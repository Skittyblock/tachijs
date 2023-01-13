package okhttp3

class Headers internal constructor(
    internal val namesAndValues: Array<String>
) : Iterable<Pair<String, String>> {
    operator fun get(name: String): String? {
        for (i in namesAndValues.size - 2 downTo 0 step 2) {
            if (name.equals(namesAndValues[i], ignoreCase = true)) {
                return namesAndValues[i + 1]
            }
        }
        return null
    }

    val size: Int
        get() = namesAndValues.size / 2

    fun name(index: Int): String =
        namesAndValues.getOrNull(index * 2) ?: throw IndexOutOfBoundsException("name[$index]")

    fun value(index: Int): String =
        namesAndValues.getOrNull(index * 2 + 1) ?: throw IndexOutOfBoundsException("value[$index]")

    fun newBuilder(): Builder {
        val result = Builder()
        result.namesAndValues += namesAndValues
        return result
    }

    class Builder {
        internal val namesAndValues: MutableList<String> = ArrayList(20)

//        fun add(line: String) = apply {
//            val index = line.indexOf(':')
//            require(index != -1) { "Unexpected header: $line" }
//            add(line.substring(0, index).trim(), line.substring(index + 1))
//        }

        fun add(name: String, value: String) = apply {
            namesAndValues.add(name)
            namesAndValues.add(value.trim())
        }

        fun removeAll(name: String) = apply {
            var i = 0
            while (i < namesAndValues.size) {
                if (name.equals(namesAndValues[i], ignoreCase = true)) {
                    namesAndValues.removeAt(i) // name
                    namesAndValues.removeAt(i) // value
                    i -= 2
                }
                i += 2
            }
        }

        operator fun set(name: String, value: String) = apply {
            removeAll(name)
            add(name, value)
        }

        fun build(): Headers = Headers(namesAndValues.toTypedArray())
    }

    override fun iterator(): Iterator<Pair<String, String>> {
        return Array(size) { name(it) to value(it) }.iterator()
    }

    // override operator fun iterator(): Iterator<Pair<String, String>> = commonIterator()
}