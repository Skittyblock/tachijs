package uy.kohesive.injekt.api

import android.app.Application

interface InjektFactory {
    fun <R: Any> getInstance(forType: Type): R
    fun <R: Any> getInstanceOrElse(forType: Type, default: R): R
    fun <R: Any> getInstanceOrElse(forType: Type, default: ()->R): R
    fun <R: Any> getInstanceOrNull(forType: Type): R?

    fun <R: Any, K: Any> getKeyedInstance(forType: Type, key: K): R
    fun <R: Any, K: Any> getKeyedInstanceOrElse(forType: Type, key: K, default: R): R
    fun <R: Any, K: Any> getKeyedInstanceOrElse(forType: Type, key: K, default: ()->R): R
    fun <R: Any, K: Any> getKeyedInstanceOrNull(forType: Type, key: K): R?

    fun <R: Any> getLogger(expectedLoggerType: Type, byName: String): R
//    fun <R: Any, T: Any> getLogger(expectedLoggerType: Type, forClass: Class<T>): R
}

//inline fun <R: Any> InjektFactory.get(forType: TypeReference<R>): R = getInstance(forType.type)

inline fun <reified R: Any> InjektFactory.get(): R {
    return when (R::class) {
        Application::class -> Application() as R
        else -> getInstance(fullType<R>().type)
    }
}
