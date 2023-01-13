package okhttp3.internal.connection

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.*
import org.khronos.webgl.ArrayBuffer
import tachijs.fetch
import tachijs.await

fun String.source() = object : Source {
    var offset = 0
    override fun close() {}
    override fun read(sink: Buffer, byteCount: Long): Long {
        val len = if (byteCount > length) length else byteCount.toInt()
        val s = slice(offset until offset + len)
        val str = ByteString.of(s.toByte())
        offset += len
        sink.write(str)
        return len.toLong()
    }
    override fun timeout(): Timeout {
        return Timeout.NONE
    }
}

class RealCall(
    val client: OkHttpClient,
    val originalRequest: Request,
    val forWebSocket: Boolean
) : Call {
    private var cancelled = false

    override fun request() = originalRequest

    override suspend fun execute(): Response {
        console.log("req:", request().url.toString())
        val req = fetch(
            originalRequest.method,
            "https://cors-anywhere.herokuapp.com/" +
            originalRequest.url.toString()
        ).await()
        val text = req.text().await()
        val buffer = Buffer()
        buffer.writeUtf8(text)
//        try {
//            console.log("body:", buffer.readUtf8())
//        } catch (e: Exception) {
//            console.log("error:", e.message)
//        }
        return Response.Builder()
            .request(originalRequest)
            .code(req.status.toInt())
            .body(buffer.asResponseBody(null, text.length.toLong()))
            .build()
    }

    override fun cancel() {
        if (cancelled) return
        cancelled = true
    }

    override fun isExecuted(): Boolean = false

    override fun isCanceled(): Boolean = cancelled

    override fun clone() = RealCall(client, originalRequest, forWebSocket)
}