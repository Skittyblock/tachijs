package tachijs

import kotlinx.browser.window
import org.w3c.fetch.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.*

suspend fun <T> Promise<T>.await(): T = suspendCoroutine { cont ->
    then({ cont.resume(it) }, { cont.resumeWithException(it) })
}

fun fetch(
    method: String,
    url: String,
    body: dynamic = null,
    headers: Json = json("pragma" to "no-cache")
): Promise<Response> =
    window.fetch(
        url, RequestInit(
            method = method,
            body = body,
            headers = headers
        )
    )