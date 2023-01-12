package rx

interface Subscription {
    fun isUnsubscribed(): Boolean
    fun unsubscribe()
}
