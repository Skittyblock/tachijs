package org.jsoup.select

import kotlinx.dom.isElement
import org.jsoup.nodes.Element
import org.w3c.dom.NodeList
import org.w3c.dom.asList

class Elements(
    private val nodeList: NodeList?
): ArrayList<Element>() {

    init {
        if (nodeList != null) {
            for (node in nodeList.asList()) {
                if (node.isElement) {
                    val el = Element(node as org.w3c.dom.Element)
                    add(el)
                }
            }
        }
    }

    fun text(): String {
        val sb: StringBuilder = StringBuilder(8 * 1024) // StringUtil.borrowBuilder()
        for (element in this) {
            if (sb.isNotEmpty()) sb.append(" ")
            sb.append(element.text())
        }
        return sb.toString() // StringUtil.releaseBuilder(sb)
    }

    fun attr(attributeKey: String): String {
        for (element in this) {
            if (element.hasAttr(attributeKey)) return element.attr(attributeKey)
        }
        return ""
    }

    fun select(query: String): Elements {
        return Selector.select(query, this)
    }
}