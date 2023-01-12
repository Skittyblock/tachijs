package rx.functions

interface FuncN<R> : Function {
    fun call(vararg args: Any?): R
}
