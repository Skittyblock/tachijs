package rx

import rx.functions.Action1
import rx.functions.Func1
import rx.internal.operators.OnSubscribeMap
import rx.internal.util.ScalarSynchronousObservable


open class Observable<T>(
    val onSubscribe: OnSubscribe<T>?
) {
    companion object {
        fun <T> just(value: T): Observable<T> {
            return ScalarSynchronousObservable.create(value)
        }

        fun <T> unsafeCreate(f: OnSubscribe<T>?): Observable<T> {
            return Observable(f)
        }
    }

    fun <R> map(func: Func1<in T, out R>): Observable<R> {
        return unsafeCreate(OnSubscribeMap<T, R>(this, func))
    }

    fun interface OnSubscribe<T> : Action1<Subscriber<T>> { // cover for generics insanity
//        override fun call(subscriber: Subscriber<T>)
    }

    fun unsafeSubscribe(subscriber: Subscriber<T>): Subscription {
        subscriber.onStart()
        onSubscribe?.call(subscriber)
        return subscriber
//        return try {
//            subscriber.onStart()
            // allow the hook to intercept and/or decorate

//            RxJavaHooks.onObservableStart(this, onSubscribe).call(subscriber)
//            RxJavaHooks.onObservableReturn(subscriber)
//            return subscriber
//        } catch (e: Throwable) {
            // special handling for certain Throwable/Error/Exception types
//            rx.exceptions.Exceptions.throwIfFatal(e)
            // if an unhandled error occurs executing the onSubscribe we will propagate it
//            try {
//            subscriber.onError(e)
//            throw e
//            } catch (e2: Throwable) {
//                rx.exceptions.Exceptions.throwIfFatal(e2)
//                val r: RuntimeException = OnErrorFailedException(
//                    "Error occurred attempting to subscribe [" + e.message + "] and then again while trying to pass to onError.",
//                    e2
//                )
//                RxJavaHooks.onObservableError(r)
//                throw r // NOPMD
//                throw e2
//            }
//            Subscriptions.unsubscribed()
//        }
    }
}