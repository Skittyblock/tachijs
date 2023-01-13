package rx.internal.util

import rx.Subscription

class SubscriptionList: Subscription {

    val subscriptions: MutableList<Subscription> = mutableListOf()
    var unsubscribed = false

    fun add(s: Subscription) {
        if (s.isUnsubscribed()) {
            return
        }
        if (!unsubscribed) {
            subscriptions.add(s)
        }
//        s.unsubscribe() // i don't understand this
    }

    override fun isUnsubscribed(): Boolean = unsubscribed

    override fun unsubscribe() {
        console.log("sub list unsub")
        if (!unsubscribed) {
            unsubscribed = true
            unsubscribeFromAll()
            subscriptions.clear()
        }
    }

    private fun unsubscribeFromAll() {
        if (subscriptions.isEmpty()) return
        for (s in subscriptions) {
            s.unsubscribe()
        }
    }
}
