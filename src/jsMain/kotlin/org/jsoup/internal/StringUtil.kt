package org.jsoup.internal

class StringUtil {

    companion object {
//        fun borrowBuilder(): StringBuilder? {
//            val builders: Stack<StringBuilder> = threadLocalBuilders.get()
//            return if (builders.empty()) StringBuilder(MaxCachedBuilderSize) else builders.pop()
//        }

        fun resolve(baseUrl: String?, relUrl: String): String {
            // workaround: java will allow control chars in a path URL and may treat as relative, but Chrome / Firefox will strip and may see as a scheme. Normalize to browser's view.
//            var baseUrl = baseUrl
//            var relUrl = relUrl
//            baseUrl = stripControlChars(baseUrl)
//            relUrl = stripControlChars(relUrl)
//            return try {
//                val base: URL
//                try {
//                    base = URL(baseUrl)
//                } catch (e: MalformedURLException) {
//                    // the base is unsuitable, but the attribute/rel may be abs on its own, so try that
//                    val abs = URL(relUrl)
//                    return abs.toExternalForm()
//                }
//                resolve(base, relUrl).toExternalForm()
//            } catch (e: MalformedURLException) {
//                // it may still be valid, just that Java doesn't have a registered stream handler for it, e.g. tel
//                // we test here vs at start to normalize supported URLs (e.g. HTTP -> http)
//                if (validUriScheme.matcher(relUrl).find()) relUrl else ""
//            }
            TODO("StringUtil.resolve()")
        }
    }
}