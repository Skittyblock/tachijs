package org.reactivestreams

interface Subscription {

    fun request(n: Long)

    fun cancel()
}