package rx.functions

fun interface Action2<T1, T2> : Action {
    fun call(t1: T1, t2: T2)
}
