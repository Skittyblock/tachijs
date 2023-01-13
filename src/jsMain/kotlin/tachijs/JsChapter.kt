package tachijs

import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga

external class JsChapter {

    var url: String
    var name: String
    var dateUpload: Long
    var chapterNumber: Float
    var scanlator: String?

    companion object {}
}

fun JsChapter.Companion.from(chapter: SChapter): JsChapter {
    val result = JsChapter()
    result.url = chapter.url
    result.name = chapter.name
    result.dateUpload = chapter.date_upload
    result.chapterNumber = chapter.chapter_number
    result.scanlator = chapter.scanlator
    return result
}

fun JsChapter.toChapter(): SChapter {
    val chapter = SChapter.create()
    chapter.url = url
    chapter.name = name
    chapter.date_upload = dateUpload
    chapter.chapter_number = chapterNumber
    chapter.scanlator = scanlator
    return chapter
}
