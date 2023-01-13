package rx.internal.operators

import rx.Observable
import rx.Producer
import rx.Subscriber
//import rx.exceptions.Exceptions
//import rx.exceptions.OnErrorThrowable
import rx.functions.Func1


class OnSubscribeMap<T, R>(
    val source: Observable<T>,
    val transformer: Func1<in T, out R>
) : Observable.OnSubscribe<R> {

    override fun call(t: Subscriber<R>) {
        val parent = MapSubscriber(t, transformer)
        t.add(parent)
        source.unsafeSubscribe(parent)
    }

    internal class MapSubscriber<T, R>(
        val actual: Subscriber<in R>,
        val mapper: Func1<in T, out R>
    ) : Subscriber<T>() {
        private var done = false

        override fun onNext(t: T) {
            val result: R
            result = try {
                mapper.call(t)
            } catch (ex: Exception) {
//                rx.exceptions.Exceptions.throwIfFatal(ex)
                console.log("call error!", ex)
                unsubscribe()
//                onError(OnErrorThrowable.addValueAsLastCause(ex, t))
                return
            }
            actual.onNext(result)
        }

        override fun onError(e: Throwable) {
            if (done) {
//                RxJavaHooks.onError(e)
                return
            }
            done = true
            actual.onError(e)
        }

        override fun onCompleted() {
            if (done) {
                return
            }
            actual.onCompleted()
        }

        override fun setProducer(p: Producer) {
            actual.setProducer(p)
        }
    }
}
