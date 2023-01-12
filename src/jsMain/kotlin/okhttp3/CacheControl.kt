package okhttp3

import kotlin.time.toDuration
import kotlin.time.DurationUnit

// https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/TimeUnit.html
//enum class TimeUnit {
//    DAYS, HOURS, MICROSECONDS, MILLISECONDS, MINUTES, NANOSECONDS, SECONDS
//
//    fun toSeconds(duration: Long): Long {
//        when (this) {
//            MINUTES -> duration * 60
//            else -> 0
//        }
//    }
//}

class CacheControl internal constructor(
    val noCache: Boolean,
    val noStore: Boolean,
    val maxAgeSeconds: Int,
    val sMaxAgeSeconds: Int,
) {
    class Builder {
        internal var noCache: Boolean = false
        internal var noStore: Boolean = false
        internal var maxAgeSeconds = -1

        fun maxAge(maxAge: Int, timeUnit: DurationUnit) = apply {
            require(maxAge >= 0) { "maxAge < 0: $maxAge" }
            val maxAgeSecondsLong = maxAge.toDuration(timeUnit).inWholeSeconds
            this.maxAgeSeconds = maxAgeSecondsLong.commonClampToInt()
        }

        internal fun Long.commonClampToInt(): Int {
            return when {
                this > Int.MAX_VALUE -> Int.MAX_VALUE
                else -> toInt()
            }
        }

        fun build(): CacheControl {
            return CacheControl(
                noCache = noCache,
                noStore = noStore,
                maxAgeSeconds = maxAgeSeconds,
                sMaxAgeSeconds = -1,
//                isPrivate = false,
//                isPublic = false,
//                mustRevalidate = false,
//                maxStaleSeconds = maxStaleSeconds,
//                minFreshSeconds = minFreshSeconds,
//                onlyIfCached = onlyIfCached,
//                noTransform = noTransform,
//                immutable = immutable,
//                headerValue = null
            )
        }
    }
}