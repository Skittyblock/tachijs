package org.jsoup.nodes

import org.jsoup.select.Elements
import org.jsoup.select.Selector
import org.w3c.dom.Document

class Document(
    var document: Document
) : Element(null) {

    override fun select(query: String): Elements {
        return Selector.select(query, this)
    }
}
