package rx

import rx.functions.Action0
import rx.functions.Action1
import rx.functions.Actions
import rx.functions.Func1
import rx.internal.operators.OnSubscribeDoOnEach
import rx.internal.operators.OnSubscribeMap
import rx.internal.operators.OnSubscribeThrow
import rx.internal.util.ActionObserver
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

        fun <T> error(exception: Throwable): Observable<T> {
            return unsafeCreate(OnSubscribeThrow<T>(exception))
        }
    }

    fun doOnNext(onNext: Action1<T>): Observable<T> {
        val onError: Action1<Throwable> = Actions.empty<Throwable, Any, Any, Any, Any, Any, Any, Any, Any>()
        val onCompleted: Action0 = Actions.empty()
        val observer: Observer<T> = ActionObserver<T>(onNext, onError, onCompleted)
        return unsafeCreate(OnSubscribeDoOnEach<T>(this, observer))
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