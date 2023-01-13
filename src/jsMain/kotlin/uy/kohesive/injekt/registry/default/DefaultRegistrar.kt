package uy.kohesive.injekt.registry.default

import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.Type
import uy.kohesive.injekt.api.TypeReference

open class DefaultRegistrar : InjektRegistrar {
    override fun <T : Any> addSingleton(forType: TypeReference<T>, singleInstance: T) {
        TODO("Not yet implemented")
    }

    override fun <R : Any> addSingletonFactory(forType: TypeReference<R>, factoryCalledOnce: () -> R) {
        TODO("Not yet implemented")
    }

    override fun <R : Any> addFactory(forType: TypeReference<R>, factoryCalledEveryTime: () -> R) {
        TODO("Not yet implemented")
    }

    override fun <R : Any> addPerThreadFactory(forType: TypeReference<R>, factoryCalledOncePerThread: () -> R) {
        TODO("Not yet implemented")
    }

    override fun <R : Any, K : Any> addPerKeyFactory(forType: TypeReference<R>, factoryCalledPerKey: (K) -> R) {
        TODO("Not yet implemented")
    }

    override fun <R : Any, K : Any> addPerThreadPerKeyFactory(
        forType: TypeReference<R>,
        factoryCalledPerKeyPerThread: (K) -> R
    ) {
        TODO("Not yet implemented")
    }

    override fun <O : Any, T : O> addAlias(
        existingRegisteredType: TypeReference<T>,
        otherAncestorOrInterface: TypeReference<O>
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> hasFactory(forType: TypeReference<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun <R : Any> getInstance(forType: Type): R {
        TODO("Not yet implemented")
    }

    override fun <R : Any> getInstanceOrElse(forType: Type, default: R): R {
        TODO("Not yet implemented")
    }

    override fun <R : Any> getInstanceOrElse(forType: Type, default: () -> R): R {
        TODO("Not yet implemented")
    }

    override fun <R : Any> getInstanceOrNull(forType: Type): R? {
        TODO("Not yet implemented")
    }

    override fun <R : Any, K : Any> getKeyedInstance(forType: Type, key: K): R {
        TODO("Not yet implemented")
    }

    override fun <R : Any, K : Any> getKeyedInstanceOrElse(forType: Type, key: K, default: R): R {
        TODO("Not yet implemented")
    }

    override fun <R : Any, K : Any> getKeyedInstanceOrElse(forType: Type, key: K, default: () -> R): R {
        TODO("Not yet implemented")
    }

    override fun <R : Any, K : Any> getKeyedInstanceOrNull(forType: Type, key: K): R? {
        TODO("Not yet implemented")
    }

    override fun <R : Any> getLogger(expectedLoggerType: Type, byName: String): R {
        TODO("Not yet implemented")
    }
}
