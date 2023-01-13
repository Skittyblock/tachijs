package rx.internal.operators

import rx.Observable
import rx.Subscriber

class OnSubscribeThrow<T>(private val exception: Throwable) : Observable.OnSubscribe<T> {
    override fun call(t: Subscriber<T>) {
        t.onError(exception)
    }
}
