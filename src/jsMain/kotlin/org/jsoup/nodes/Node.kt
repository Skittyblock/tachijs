package org.jsoup.nodes

import org.jsoup.internal.StringUtil
import org.w3c.dom.Node

abstract class Node(
    val node: Node?,
    val parentNode: org.jsoup.nodes.Node? = null
) {

//    abstract fun nodeName(): String
    abstract fun hasAttributes(): Boolean

    abstract fun attributes(): Attributes

    open fun absUrl(attributeKey: String): String {
        if (attributeKey.isEmpty()) return ""
        return if (!(hasAttributes() && attributes().hasKeyIgnoreCase(attributeKey))) "" else StringUtil.resolve(
            baseUri(),
            attributes().getIgnoreCase(attributeKey)
        )
    }

    open fun attr(attributeKey: String): String {
        if (!hasAttributes()) return ""
        val `val`: String = attributes().getIgnoreCase(attributeKey)
        return if (`val`.isNotEmpty()) `val` else if (attributeKey.startsWith("abs:")) absUrl(attributeKey.substring("abs:".length)) else ""
    }

    open fun hasAttr(attributeKey: String): Boolean {
        if (!hasAttributes()) return false
        if (attributeKey.startsWith("abs:")) {
            val key = attributeKey.substring("abs:".length)
            if (attributes().hasKeyIgnoreCase(key) && !absUrl(key).isEmpty()) return true
        }
        return attributes().hasKeyIgnoreCase(attributeKey)
    }

    abstract fun baseUri(): String?
}