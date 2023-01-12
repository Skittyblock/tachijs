package rx

interface Observer<T> {

    fun onCompleted()
    fun onError(e: Throwable)
    fun onNext(t: T)
}
