package okhttp3

import okhttp3.internal.connection.RealCall
import kotlin.time.DurationUnit
import kotlin.time.toDuration

open class OkHttpClient internal constructor(
    builder: Builder
) : Call.Factory {

    val interceptors: List<Interceptor> =
        builder.interceptors.toList()
    val callTimeoutMillis: Int = builder.callTimeout
    val connectTimeoutMillis: Int = builder.connectTimeout
    val readTimeoutMillis: Int = builder.readTimeout
    val writeTimeoutMillis: Int = builder.writeTimeout

    override fun newCall(request: Request): Call = RealCall(this, request, forWebSocket = false)

    open fun newBuilder(): Builder = Builder(this)

    class Builder() {
        internal val interceptors: MutableList<Interceptor> = mutableListOf()
        internal var callTimeout = 0
        internal var connectTimeout = 10_000
        internal var readTimeout = 10_000
        internal var writeTimeout = 10_000

        internal constructor(okHttpClient: OkHttpClient) : this() {
            this.interceptors += okHttpClient.interceptors
            this.callTimeout = okHttpClient.callTimeoutMillis
            this.connectTimeout = okHttpClient.connectTimeoutMillis
            this.readTimeout = okHttpClient.readTimeoutMillis
            this.writeTimeout = okHttpClient.writeTimeoutMillis
        }

        fun interceptors(): MutableList<Interceptor> = interceptors

        fun addInterceptor(interceptor: Interceptor) = apply {
            interceptors += interceptor
        }

        internal fun checkDuration(name: String, duration: Long, unit: DurationUnit?): Int {
            check(duration >= 0L) { "$name < 0" }
            check(unit != null) { "unit == null" }
            val millis = duration.toDuration(unit).inWholeMilliseconds
            require(millis <= Int.MAX_VALUE) { "$name too large." }
            require(millis != 0L || duration <= 0L) { "$name too small." }
            return millis.toInt()
        }

        fun callTimeout(timeout: Long, unit: DurationUnit) = apply {
            callTimeout = checkDuration("timeout", timeout, unit)
        }

        fun connectTimeout(timeout: Long, unit: DurationUnit) = apply {
            connectTimeout = checkDuration("timeout", timeout, unit)
        }

        fun readTimeout(timeout: Long, unit: DurationUnit) = apply {
            readTimeout = checkDuration("timeout", timeout, unit)
        }

        fun writeTimeout(timeout: Long, unit: DurationUnit) = apply {
            writeTimeout = checkDuration("timeout", timeout, unit)
        }

        fun build() = OkHttpClient(this)
    }
}