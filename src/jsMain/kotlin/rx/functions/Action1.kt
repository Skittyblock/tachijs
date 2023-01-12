package rx.functions

fun interface Action1<T> : Action {
    fun call(t: T)
}
