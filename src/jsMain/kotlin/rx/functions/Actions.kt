package rx.functions

interface Actions {

    companion object {
//        private val EMPTY_ACTION = EmptyAction()

        fun <T0, T1, T2, T3, T4, T5, T6, T7, T8> empty(): EmptyAction<T0, T1, T2, T3, T4, T5, T6, T7, T8> {
            return EmptyAction()
        }

        fun empty(): EmptyAction<Any, Any, Any, Any, Any, Any, Any, Any, Any> {
            return EmptyAction()
        }

        class EmptyAction<T0, T1, T2, T3, T4, T5, T6, T7, T8> : Action0, Action1<T0>,
            Action2<T0, T1>, Action3<T0, T1, T2>, Action4<T0, T1, T2, T3>, Action5<T0, T1, T2, T3, T4>,
            Action6<T0, T1, T2, T3, T4, T5>, Action7<T0, T1, T2, T3, T4, T5, T6>,
            Action8<T0, T1, T2, T3, T4, T5, T6, T7>,
            Action9<T0, T1, T2, T3, T4, T5, T6, T7, T8>, ActionN
        {
            override fun call() {}
            override fun call(t1: T0) {}
            override fun call(t1: T0, t2: T1) {}
            override fun call(t1: T0, t2: T1, t3: T2) {}
            override fun call(t1: T0, t2: T1, t3: T2, t4: T3) {}
            override fun call(t1: T0, t2: T1, t3: T2, t4: T3, t5: T4) {}
            override fun call(t1: T0, t2: T1, t3: T2, t4: T3, t5: T4, t6: T5) {}
            override fun call(t1: T0, t2: T1, t3: T2, t4: T3, t5: T4, t6: T5, t7: T6) {}
            override fun call(t1: T0, t2: T1, t3: T2, t4: T3, t5: T4, t6: T5, t7: T6, t8: T7) {}
            override fun call(t1: T0, t2: T1, t3: T2, t4: T3, t5: T4, t6: T5, t7: T6, t8: T7, t9: T8) {}
            override fun call(vararg args: Any?) {}
        }
    }

}