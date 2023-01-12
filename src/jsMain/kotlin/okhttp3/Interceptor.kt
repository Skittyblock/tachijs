package okhttp3

fun interface Interceptor {
    fun intercept(chain: Chain): Response

    companion object {
        inline operator fun invoke(crossinline block: (chain: Chain) -> Response): Interceptor =
            Interceptor { block(it) }
    }

    interface Chain {
        fun request(): Request
        fun proceed(request: Request): Response
        fun call(): Call
    }
}