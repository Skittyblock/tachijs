package rx.functions

fun interface Func1<T, R> : Function {
    fun call(t: T): R
}
