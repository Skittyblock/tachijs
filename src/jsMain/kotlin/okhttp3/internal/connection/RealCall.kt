package okhttp3.internal.connection

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.w3c.xhr.XMLHttpRequest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.*

class RealCall(
    val client: OkHttpClient,
    val originalRequest: Request,
    val forWebSocket: Boolean
) : Call {
    private var cancelled = false

    override fun request() = originalRequest

    internal fun getFile(url: String): Promise<XMLHttpRequest> {
        val p = Promise<XMLHttpRequest> {
                resolve, reject ->
            val xhr = XMLHttpRequest()
            xhr.open("GET", url)
            xhr.addEventListener("load", { e ->
                resolve(xhr)
            })
            xhr.send()
        }
        return p
    }

    suspend fun <T> Promise<T>.await(): T = suspendCoroutine { cont ->
        then({ cont.resume(it) }, { cont.resumeWithException(it) })
    }

    override suspend fun execute(): Response {
//        TODO("RealCall.execute() not implemented!")
//        js("fetch('https://skitty.xyz')" )
        console.log("req:", request())
        val req =  getFile("https://cors-anywhere.herokuapp.com/https://skitty.xyz").await()
        console.log("got:", req)
        return Response.Builder()
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