package uy.kohesive.injekt.api

interface InjektRegistry {
    fun <T : Any> addSingleton(forType: TypeReference<T>, singleInstance: T)
    fun <R: Any> addSingletonFactory(forType: TypeReference<R>, factoryCalledOnce: () -> R)
    fun <R: Any> addFactory(forType: TypeReference<R>, factoryCalledEveryTime: () -> R)
    fun <R: Any> addPerThreadFactory(forType: TypeReference<R>, factoryCalledOncePerThread: () -> R)
    fun <R: Any, K: Any> addPerKeyFactory(forType: TypeReference<R>, factoryCalledPerKey: (K) -> R)
    fun <R: Any, K: Any> addPerThreadPerKeyFactory(forType: TypeReference<R>, factoryCalledPerKeyPerThread: (K) -> R)
//    fun <R : Any> addLoggerFactory(forLoggerType: TypeReference<R>, factoryByName: (String) -> R, factoryByClass: (Class<Any>) -> R)
    fun <O: Any, T: O> addAlias(existingRegisteredType: TypeReference<T>, otherAncestorOrInterface: TypeReference<O>)
    fun <T: Any> hasFactory(forType: TypeReference<T>): Boolean
}
