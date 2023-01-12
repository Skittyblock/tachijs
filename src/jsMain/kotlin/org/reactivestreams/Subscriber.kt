package org.reactivestreams

interface Subscriber<T> {
    fun onSubscribe(s: Subscription)

    fun onNext(t: T)

    fun onError(t: Throwable)

    fun onComplete()
}