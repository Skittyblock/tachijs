package android.content

abstract class Context {
    abstract fun getSharedPreferences(name: String, mode: Int): SharedPreferences
}