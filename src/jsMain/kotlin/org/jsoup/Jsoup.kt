package org.jsoup

import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

class Jsoup {

    companion object {
        fun parse(html: String, baseUri: String): Document {
            return Parser.parse(html, baseUri)
        }
    }
}