package org.jsoup.nodes

import org.jsoup.select.Elements
import org.jsoup.select.Selector
import org.w3c.dom.Document
import org.w3c.dom.asList

open class Element(
    var element: org.w3c.dom.Element?
) : Node(element) {

    private var attr: Attributes? = null

    init {
        if (element != null) {
            val keys: ArrayList<String> = arrayListOf()
            val vals: ArrayList<Any?> = arrayListOf()
            for (a in element!!.attributes.asList()) {
                keys.add(a.name)
                vals.add(a.value)
            }
            attr = Attributes(keys.toTypedArray(), vals.toTypedArray())
        }
    }

    override fun baseUri(): String? {
        console.log("baseuri not implemented")
        return null
    }

    override fun attributes(): Attributes = attr!!

    override fun hasAttributes(): Boolean {
        return attr != null
    }

    open fun select(query: String): Elements {
//        console.log("element select")
        return Selector.select(query, this)
    }

    fun text(): String {
        return element?.textContent ?: ""
    }
}