package android.content

open class ContextWrapper : Context() {

    override fun getSharedPreferences(name: String, mode: Int): SharedPreferences {
        return object : SharedPreferences {
            override fun edit(): SharedPreferences.Editor {
                return object : SharedPreferences.Editor {
                    override fun apply() {}
                    override fun putLong(key: String, value: Long) = apply {  }
                }
            }

            override fun contains(key: String): Boolean = false

            override fun getLong(key: String, defValue: Long): Long = defValue
        }
    }
}