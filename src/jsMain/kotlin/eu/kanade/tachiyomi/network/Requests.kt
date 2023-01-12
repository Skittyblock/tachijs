package eu.kanade.tachiyomi.network

import okhttp3.CacheControl
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.HttpUrl.Companion.toHttpUrl
import kotlin.time.DurationUnit

//import okhttp3.RequestBody

private val DEFAULT_CACHE_CONTROL = CacheControl.Builder().maxAge(10, DurationUnit.MINUTES).build()
private val DEFAULT_HEADERS: Headers = Headers.Builder().build()
//private val DEFAULT_BODY: RequestBody = throw Exception("Stub!")

fun GET(
    url: String,
    headers: Headers = DEFAULT_HEADERS,
    cache: CacheControl = DEFAULT_CACHE_CONTROL
): Request {
    return GET(url.toHttpUrl(), headers, cache)
}

/**
 * @since extensions-lib 1.4
 */
fun GET(
    url: HttpUrl,
    headers: Headers = DEFAULT_HEADERS,
    cache: CacheControl = DEFAULT_CACHE_CONTROL,
): Request {
    return Request.Builder()
        .url(url)
        .headers(headers)
        .cacheControl(cache)
        .build()
}

//fun POST(url: String,
//         headers: Headers = DEFAULT_HEADERS,
//         body: RequestBody = DEFAULT_BODY,
//         cache: CacheControl = DEFAULT_CACHE_CONTROL): Request {
//
//    throw Exception("Stub!")
//}
