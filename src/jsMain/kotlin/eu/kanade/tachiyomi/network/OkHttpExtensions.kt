package eu.kanade.tachiyomi.network

import okhttp3.Call
import okhttp3.Response
import rx.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun launch(block: suspend () -> Unit) {
    block.startCoroutine(object : Continuation<Unit> {
        override val context: CoroutineContext get() = EmptyCoroutineContext
        override fun resumeWith(result: Result<Unit>) {
        }
    })
}

fun Call.asObservable(): Observable<Response> {
    return Observable.unsafeCreate { subscriber ->
        val call = clone()

        val requestArbitrator = object : Producer, Subscription {
            override fun request(n: Long) {
                launch {
                    try {
                        val response = call.execute()
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(response)
                            subscriber.onCompleted()
                        } else {
                            console.log("no")
                        }
                    } catch (e: Exception) {
                        console.log("aaa")
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(e)
                        }
                    }
                }
            }

            override fun unsubscribe() {
                call.cancel()
            }

            override fun isUnsubscribed(): Boolean {
                return call.isCanceled()
            }
        }

        subscriber.add(requestArbitrator)
        subscriber.setProducer(requestArbitrator)
    }
}

fun Call.asObservableSuccess(): Observable<Response> = asObservable()
//
//suspend fun Call.await(): Response = throw Exception("Stub!")
