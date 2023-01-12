package rx.functions

fun interface ActionN : Action {
    fun call(vararg args: Any?)
}
