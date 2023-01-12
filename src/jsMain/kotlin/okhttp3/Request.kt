package okhttp3

class Request internal constructor(
    builder: Builder
) {
    val method: String = builder.method
    val headers: Headers = builder.headers.build()

    open class Builder {
        var url: HttpUrl? = null
        internal var method: String
        internal var headers: Headers.Builder

        constructor() {
            this.method = "GET"
            this.headers = Headers.Builder()
        }

        open fun url(url: HttpUrl): Builder = apply {
            this.url = url
        }

        fun headers(headers: Headers) = apply {
            this.headers = headers.newBuilder()
        }

        fun header(name: String, value: String) = apply {
            headers[name] = value
        }

        fun addHeader(name: String, value: String) = apply {
            headers.add(name, value)
        }

        fun removeHeader(name: String) = apply {
            headers.removeAll(name)
        }

        fun cacheControl(cacheControl: CacheControl): Builder {
            val value = cacheControl.toString()
            return when {
                value.isEmpty() -> removeHeader("Cache-Control")
                else -> header("Cache-Control", value)
            }
        }

        open fun build(): Request = Request(this)
    }
}