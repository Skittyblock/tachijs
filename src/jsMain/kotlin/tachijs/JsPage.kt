package tachijs

import eu.kanade.tachiyomi.source.model.Page

external class JsPage {
    var index: Int
    var url: String
    var imageUrl: String?

    companion object {}
}

fun JsPage.Companion.from(page: Page): JsPage {
    val result = JsPage()
    result.index = page.index
    result.url = page.url
    result.imageUrl = page.imageUrl
    return result
}
