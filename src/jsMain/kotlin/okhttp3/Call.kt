package okhttp3

interface Call {
    fun request(): Request

//    @Throws(IOException::class)
    suspend fun execute(): Response

    fun cancel()

    fun isExecuted(): Boolean
    fun isCanceled(): Boolean

//    fun timeout(): Timeout
    fun clone(): Call

    fun interface Factory {
        fun newCall(request: Request): Call
    }
}