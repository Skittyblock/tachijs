package android.content

interface SharedPreferences {

    fun edit(): Editor

    fun contains(key: String): Boolean

    fun getLong(key: String, defValue: Long): Long

    interface Editor {
        fun apply()

        fun putLong(key: String, value: Long): Editor
    }
}
