package org.jsoup.parser

import org.jsoup.nodes.Document
import org.w3c.dom.parsing.DOMParser

class Parser {

    companion object {
        fun parse(html: String, baseUri: String): Document {
            val doc = DOMParser().parseFromString(html, "text/html")
            return Document(doc)
        }
    }
}