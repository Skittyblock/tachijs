package org.reactivestreams

interface Publisher<T> {
    fun subscribe(s: Subscriber<in T>)
}
