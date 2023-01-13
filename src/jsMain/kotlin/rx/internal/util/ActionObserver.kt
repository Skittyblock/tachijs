package rx.internal.util

import rx.Observer
import rx.functions.Action0
import rx.functions.Action1

class ActionObserver<T>(
    val onNext: Action1<T>,
    val onError: Action1<Throwable>,
    val onComplete: Action0
) : Observer<T> {

    override fun onNext(t: T) {
        onNext.call(t)
    }

    override fun onError(e: Throwable) {
        onError.call(e)
    }

    override fun onCompleted() {
        onComplete.call()
    }
}