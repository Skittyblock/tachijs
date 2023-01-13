package okhttp3

import okio.Buffer
import okio.BufferedSource
import okio.use

abstract class ResponseBody {

    abstract fun contentType(): MediaType?
    abstract fun contentLength(): Long
//    fun byteStream(): InputStream = source().inputStream()
    abstract fun source(): BufferedSource

    fun close() = source().close()

    fun string(): String = source().use { source ->
//        source.readString(charset = source.readBomAsCharset(charset()))
        source.readUtf8()
    }

//    private fun charset() = contentType().charset()

    companion object {
        fun ByteArray.toResponseBody(contentType: MediaType? = null): ResponseBody {
            return Buffer()
                .write(this)
                .asResponseBody(contentType, size.toLong())
        }

        fun BufferedSource.asResponseBody(
            contentType: MediaType?,
            contentLength: Long
        ): ResponseBody = object : ResponseBody() {
            override fun contentType(): MediaType? = contentType

            override fun contentLength(): Long = contentLength

            override fun source(): BufferedSource = this@asResponseBody
        }

        fun create(
            contentType: MediaType?,
            contentLength: Long,
            content: BufferedSource
        ): ResponseBody = content.asResponseBody(contentType, contentLength)
    }
}