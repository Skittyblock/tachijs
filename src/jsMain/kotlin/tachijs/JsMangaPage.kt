package tachijs

import eu.kanade.tachiyomi.source.model.MangasPage

external class JsMangasPage {
    var mangas: Array<JsManga>
    var hasNextPage: Boolean

    companion object {}
}

fun JsMangasPage.Companion.from(page: MangasPage): JsMangasPage {
    val result = JsMangasPage()
    result.mangas = page.mangas.map { JsManga.from(it) }.toTypedArray()
    result.hasNextPage = page.hasNextPage
    return result
}
