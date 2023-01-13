package uy.kohesive.injekt.api

inline fun <reified T: Any> fullType(): FullTypeReference<T> = object:FullTypeReference<T>(){}

interface TypeReference<T> {
    val type: Type
}

abstract class FullTypeReference<T> protected constructor() : TypeReference<T> {
    override val type: Type = Type() /*javaClass.getGenericSuperclass().let { superClass ->
        if (superClass is Class<*>) {
            throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")
        }
        (superClass as ParameterizedType).getActualTypeArguments()[0]
    }*/
}

class Type {

}
