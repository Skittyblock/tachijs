package org.jsoup.select

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.w3c.dom.NodeList

class Selector {

    companion object {
        fun select(query: String, roots: Iterable<Element>): Elements {
            val elements = Elements(null)
            val seenElements = HashSet<org.w3c.dom.Element>()
            for (root in roots) {
                val found = select(query, root)
                for (el in found) {
                    if (seenElements.add(el.element!!)) {
                        elements.add(el)
                    }
                }
            }
            return elements
        }

        fun select(query: String, root: Document): Elements {
            val list = root.document.querySelectorAll(query)
            return Elements(list)
        }

        fun select(query: String, root: Element): Elements {
            val list = root.element?.querySelectorAll(query)
            return Elements(list)
        }
    }
}