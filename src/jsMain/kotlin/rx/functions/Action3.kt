package rx.functions

fun interface Action3<T1, T2, T3> : Action {
    fun call(t1: T1, t2: T2, t3: T3)
}
