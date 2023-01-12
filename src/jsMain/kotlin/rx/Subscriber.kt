package rx

import rx.internal.util.SubscriptionList

abstract class Subscriber<T>: Observer<T>, Subscription {

    private val NOT_SET = Long.MIN_VALUE

    private val subscriptions = SubscriptionList()
    private var subscriber: Subscriber<T>? = null
    private var producer: Producer? = null
    private var requested: Long = NOT_SET

    fun add(s: Subscription) {
        subscriptions.add(s)
    }

    override fun unsubscribe() {
        subscriptions.unsubscribe()
    }

    override fun isUnsubscribed() = subscriptions.isUnsubscribed()

    open fun onStart() {
        // do nothing
    }

    open fun setProducer(p: Producer) {
        var passToSubscriber = false
        this.producer = p
        if (passToSubscriber) {
            TODO("pass to subscriber")
        } else {
            if (requested == NOT_SET) {
                producer?.request(Long.MAX_VALUE)
            } else {
                producer?.request(requested)
            }
        }
    }
}
