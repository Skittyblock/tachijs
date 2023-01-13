package org.jsoup.nodes

class Attributes : Iterable<Attribute> {

    private var size = 0

    var keys: Array<String> = arrayOf()
    var vals: Array<Any?> = arrayOf()

    constructor(keys: Array<String>, vals: Array<Any?>) {
        this.keys = keys
        this.vals = vals
        this.size = keys.size
    }

    private fun indexOfKeyIgnoreCase(key: String): Int {
        for (i in 0..size) {
            if (key.equals(keys[i], ignoreCase = true)) return i
        }
        return -1
    }

    fun getIgnoreCase(key: String): String {
        val i = indexOfKeyIgnoreCase(key)
        return if (i == -1) "" else vals[i]!! as String
    }

    fun hasKeyIgnoreCase(key: String): Boolean {
        return indexOfKeyIgnoreCase(key) != -1
    }

    override fun iterator(): Iterator<Attribute> {
        TODO("iterator() not implemented")
    }
}