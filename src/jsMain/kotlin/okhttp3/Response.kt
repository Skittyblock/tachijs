package okhttp3

import okhttp3.ResponseBody.Companion.toResponseBody

class Response internal constructor(
    val request: Request,
//    val message: String,
    val code: Int,
    val headers: Headers,
    var body: ResponseBody
) {
    val isSuccessful: Boolean = code in 200..299

    fun close() {
        body.close()
    }

    open class Builder {
        internal var request: Request? = null
        internal var code = -1
        internal var headers: Headers.Builder
        var body: ResponseBody = ByteArray(0).toResponseBody()

        constructor() {
            headers = Headers.Builder()
        }

        fun request(request: Request) = apply {
            this.request = request
        }

        fun code(code: Int) = apply {
            this.code = code
        }

        fun header(name: String, value: String) = apply {
            headers[name] = value
        }

        fun headers(headers: Headers) = apply {
            this.headers = headers.newBuilder()
        }

        fun body(body: ResponseBody) = apply {
            this.body = body
        }

        open fun build(): Response {
            return Response(
                request!!,
                code,
                headers.build(),
                body
            )
        }
    }
}