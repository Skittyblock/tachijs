package tachijs

import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.model.UpdateStrategy

external class JsManga {
    var url: String
    var title: String
    var artist: String?
    var author: String?
    var description: String?
    var genre: String?
    var status: Int
    var thumbnailUrl: String?
    var updateStrategy: UpdateStrategy
    var initialized: Boolean

    companion object {}
}

fun JsManga.Companion.from(manga: SManga): JsManga {
    val result = JsManga()
    result.url = manga.url
    result.title = manga.title
    result.author = manga.author
    result.artist = manga.artist
    result.description = manga.description
    result.genre = manga.genre
    result.status = manga.status
    result.thumbnailUrl = manga.thumbnail_url
    result.updateStrategy = manga.update_strategy
    result.initialized = manga.initialized
    return result
}

fun JsManga.toManga(): SManga {
    val manga = SManga.create()
    manga.url = url
    manga.title = title
    manga.artist = artist
    manga.author = author
    manga.description = description
    manga.genre = genre
    manga.status = status
    manga.thumbnail_url = thumbnailUrl
    manga.update_strategy = updateStrategy
    manga.initialized = initialized
    return manga
}
