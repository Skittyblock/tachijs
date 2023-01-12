package okhttp3

class Response internal constructor(
    val request: Request,
    val headers: Headers
) {
    open class Builder {
        internal var request: Request? = null
        internal var headers: Headers.Builder
//        var body: ResponseBody = commonEmptyResponse

        constructor() {
            headers = Headers.Builder()
        }

        fun request(request: Request) {
            this.request = request
        }

        open fun build(): Response {
            return Response(
                request!!,
                headers.build()
            )
        }
    }
}