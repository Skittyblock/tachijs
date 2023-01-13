package uy.kohesive.injekt.api

open class InjektScope(val registrar: InjektRegistrar) : InjektRegistrar by registrar {

}
