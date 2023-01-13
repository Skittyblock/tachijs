package rx.functions

fun interface Action5<T1, T2, T3, T4, T5> : Action {
    fun call(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5)
}
