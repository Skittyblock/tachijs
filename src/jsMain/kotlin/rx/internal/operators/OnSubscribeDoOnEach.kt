package rx.internal.operators

import rx.Observable
import rx.Observer
import rx.Subscriber

class OnSubscribeDoOnEach<T>(
    val source: Observable<T>,
    val doOnEachObserver: Observer<T>
) : Observable.OnSubscribe<T> {

    override fun call(t: Subscriber<T>) {
        source.unsafeSubscribe(DoOnEachSubscriber<T>(t, doOnEachObserver))
    }

    private class DoOnEachSubscriber<T> internal constructor(
        val subscriber: Subscriber<T>,
        val doOnEachObserver: Observer<T>
    ) : Subscriber<T>() {
        private var done = false

        override fun onCompleted() {
            if (done) {
                return
            }
            try {
                doOnEachObserver.onCompleted()
            } catch (e: Throwable) {
//                com.sun.tools.classfile.Attribute.Exceptions.throwOrReport(e, this)
                console.log("onCompleted", e)
                return
            }
            // Set `done` here so that the error in `doOnEachObserver.onCompleted()` can be noticed by observer
            done = true
            subscriber.onCompleted()
        }

        override fun onError(e: Throwable) {
            if (done) {
//                RxJavaHooks.onError(e)
                return
            }
            done = true
            try {
                doOnEachObserver.onError(e)
            } catch (e2: Throwable) {
//                com.sun.tools.classfile.Attribute.Exceptions.throwIfFatal(e2)
//                subscriber.onError(CompositeException(Arrays.asList(e, e2)))
                subscriber.onError(e)
                subscriber.onError(e2)
                return
            }
            subscriber.onError(e)
        }

        override fun onNext(value: T) {
            if (done) {
                return
            }
            try {
                doOnEachObserver.onNext(value)
            } catch (e: Throwable) {
//                com.sun.tools.classfile.Attribute.Exceptions.throwOrReport(e, this, value)
                console.log("onNext", e)
                return
            }
            subscriber.onNext(value)
        }
    }
}