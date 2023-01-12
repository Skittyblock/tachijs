package rx.internal.util

import rx.Observable

class ScalarSynchronousObservable<T>(
    t: T
) : Observable<T>(null) {

    companion object {
        fun <T> create(t: T): ScalarSynchronousObservable<T> {
            return ScalarSynchronousObservable(t)
        }
    }
}