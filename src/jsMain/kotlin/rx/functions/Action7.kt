package rx.functions

fun interface Action7<T1, T2, T3, T4, T5, T6, T7> : Action {
    fun call(t1: T1, t2: T2, t3: T3, t4: T4, t5: T5, t6: T6, t7: T7)
}
