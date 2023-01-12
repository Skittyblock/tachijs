package okhttp3

class HttpUrl internal constructor(
    val scheme: String,
    val url: String
) {
    val isHttps: Boolean
        get() = scheme == "https"

    class Builder {
        internal var scheme: String? = null
        internal var encodedUsername = ""
        internal var encodedPassword = ""
        internal var host: String? = null
        internal var port = -1
        internal val encodedPathSegments = mutableListOf<String>("")
        internal var encodedQueryNamesAndValues: MutableList<String?>? = null
        internal var encodedFragment: String? = null

        fun build(): HttpUrl = HttpUrl(
            scheme ?: throw IllegalStateException("scheme == null"),
            toString()
        )

        internal fun parse(base: HttpUrl?, input: String): Builder {
            var pos = input.indexOfFirstNonAsciiWhitespace()
            val limit = input.indexOfLastNonAsciiWhitespace(pos)

            // Scheme.
            val schemeDelimiterOffset = schemeDelimiterOffset(input, pos, limit)
            if (schemeDelimiterOffset != -1) {
                when {
                    input.startsWith("https:", ignoreCase = true, startIndex = pos) -> {
                        this.scheme = "https"
                        pos += "https:".length
                    }
                    input.startsWith("http:", ignoreCase = true, startIndex = pos) -> {
                        this.scheme = "http"
                        pos += "http:".length
                    }
                    else -> throw IllegalArgumentException("Expected URL scheme 'http' or 'https' but was '" +
                            input.substring(0, schemeDelimiterOffset) + "'")
                }
            } else if (base != null) {
                this.scheme = base.scheme
            } else {
                val truncated = if (input.length > 6) input.take(6) + "..." else input
                throw IllegalArgumentException(
                    "Expected URL scheme 'http' or 'https' but no scheme was found for $truncated")
            }

            // Authority.
//            var hasUsername = false
//            var hasPassword = false
//            val slashCount = input.slashCount(pos, limit)
//            if (slashCount >= 2 || base == null || base.scheme != this.scheme) {
//                // Read an authority if either:
//                //  * The input starts with 2 or more slashes. These follow the scheme if it exists.
//                //  * The input scheme exists and is different from the base URL's scheme.
//                //
//                // The structure of an authority is:
//                //   username:password@host:port
//                //
//                // Username, password and port are optional.
//                //   [username[:password]@]host[:port]
//                pos += slashCount
//                authority@ while (true) {
//                    val componentDelimiterOffset = input.delimiterOffset("@/\\?#", pos, limit)
//                    val c = if (componentDelimiterOffset != limit) {
//                        input[componentDelimiterOffset].code
//                    } else {
//                        -1
//                    }
//                    when (c) {
//                        '@'.code -> {
//                            // User info precedes.
//                            if (!hasPassword) {
//                                val passwordColonOffset = input.delimiterOffset(':', pos, componentDelimiterOffset)
//                                val canonicalUsername = input.canonicalize(
//                                    pos = pos,
//                                    limit = passwordColonOffset,
//                                    encodeSet = USERNAME_ENCODE_SET,
//                                    alreadyEncoded = true
//                                )
//                                this.encodedUsername = if (hasUsername) {
//                                    this.encodedUsername + "%40" + canonicalUsername
//                                } else {
//                                    canonicalUsername
//                                }
//                                if (passwordColonOffset != componentDelimiterOffset) {
//                                    hasPassword = true
//                                    this.encodedPassword = input.canonicalize(
//                                        pos = passwordColonOffset + 1,
//                                        limit = componentDelimiterOffset,
//                                        encodeSet = PASSWORD_ENCODE_SET,
//                                        alreadyEncoded = true
//                                    )
//                                }
//                                hasUsername = true
//                            } else {
//                                this.encodedPassword = this.encodedPassword + "%40" + input.canonicalize(
//                                    pos = pos,
//                                    limit = componentDelimiterOffset,
//                                    encodeSet = PASSWORD_ENCODE_SET,
//                                    alreadyEncoded = true
//                                )
//                            }
//                            pos = componentDelimiterOffset + 1
//                        }
//
//                        -1, '/'.code, '\\'.code, '?'.code, '#'.code -> {
//                            // Host info precedes.
//                            val portColonOffset = portColonOffset(input, pos, componentDelimiterOffset)
//                            if (portColonOffset + 1 < componentDelimiterOffset) {
//                                host = input.percentDecode(pos = pos, limit = portColonOffset).toCanonicalHost()
//                                port = parsePort(input, portColonOffset + 1, componentDelimiterOffset)
//                                require(port != -1) {
//                                    "Invalid URL port: \"${input.substring(portColonOffset + 1,
//                                        componentDelimiterOffset)}\""
//                                }
//                            } else {
//                                host = input.percentDecode(pos = pos, limit = portColonOffset).toCanonicalHost()
//                                port = HttpUrl.defaultPort(scheme!!)
//                            }
//                            require(host != null) {
//                                "$INVALID_HOST: \"${input.substring(pos, portColonOffset)}\""
//                            }
//                            pos = componentDelimiterOffset
//                            break@authority
//                        }
//                    }
//                }
//            } else {
//                // This is a relative link. Copy over all authority components. Also maybe the path & query.
//                this.encodedUsername = base.encodedUsername
//                this.encodedPassword = base.encodedPassword
//                this.host = base.host
//                this.port = base.port
//                this.encodedPathSegments.clear()
//                this.encodedPathSegments.addAll(base.encodedPathSegments)
//                if (pos == limit || input[pos] == '#') {
//                    encodedQuery(base.encodedQuery)
//                }
//            }
//
//            // Resolve the relative path.
//            val pathDelimiterOffset = input.delimiterOffset("?#", pos, limit)
//            resolvePath(input, pos, pathDelimiterOffset)
//            pos = pathDelimiterOffset
//
//            // Query.
//            if (pos < limit && input[pos] == '?') {
//                val queryDelimiterOffset = input.delimiterOffset('#', pos, limit)
//                this.encodedQueryNamesAndValues = input.canonicalize(
//                    pos = pos + 1,
//                    limit = queryDelimiterOffset,
//                    encodeSet = QUERY_ENCODE_SET,
//                    alreadyEncoded = true,
//                    plusIsSpace = true
//                ).toQueryNamesAndValues()
//                pos = queryDelimiterOffset
//            }
//
//            // Fragment.
//            if (pos < limit && input[pos] == '#') {
//                this.encodedFragment = input.canonicalize(
//                    pos = pos + 1,
//                    limit = limit,
//                    encodeSet = FRAGMENT_ENCODE_SET,
//                    alreadyEncoded = true,
//                    unicodeAllowed = true
//                )
//            }

            return this
        }

        internal fun schemeDelimiterOffset(input: String, pos: Int, limit: Int): Int {
            if (limit - pos < 2) return -1

            val c0 = input[pos]
            if ((c0 < 'a' || c0 > 'z') && (c0 < 'A' || c0 > 'Z')) return -1 // Not a scheme start char.

            characters@ for (i in pos + 1 until limit) {
                return when (input[i]) {
                    // Scheme character. Keep going.
                    in 'a'..'z', in 'A'..'Z', in '0'..'9', '+', '-', '.' -> continue@characters

                    // Scheme prefix!
                    ':' -> i

                    // Non-scheme character before the first ':'.
                    else -> -1
                }
            }

            return -1 // No ':'; doesn't start with a scheme.
        }

        /** Returns the number of '/' and '\' slashes in this, starting at `pos`. */
        internal fun String.slashCount(pos: Int, limit: Int): Int {
            var slashCount = 0
            for (i in pos until limit) {
                val c = this[i]
                if (c == '\\' || c == '/') {
                    slashCount++
                } else {
                    break
                }
            }
            return slashCount
        }

        /** Finds the first ':' in `input`, skipping characters between square braces "[...]". */
        internal fun portColonOffset(input: String, pos: Int, limit: Int): Int {
            var i = pos
            while (i < limit) {
                when (input[i]) {
                    '[' -> {
                        while (++i < limit) {
                            if (input[i] == ']') break
                        }
                    }
                    ':' -> return i
                }
                i++
            }
            return limit // No colon.
        }

//        internal fun parsePort(input: String, pos: Int, limit: Int): Int {
//            return try {
//                // Canonicalize the port string to skip '\n' etc.
//                val portString = input.canonicalize(pos = pos, limit = limit, encodeSet = "")
//                val i = portString.toInt()
//                if (i in 1..65535) i else -1
//            } catch (_: NumberFormatException) {
//                -1 // Invalid port.
//            }
//        }

//        internal fun String.isPercentEncoded(pos: Int, limit: Int): Boolean {
//            return pos + 2 < limit &&
//                    this[pos] == '%' &&
//                    this[pos + 1].parseHexDigit() != -1 &&
//                    this[pos + 2].parseHexDigit() != -1
//        }

        internal fun String.indexOfFirstNonAsciiWhitespace(
            startIndex: Int = 0,
            endIndex: Int = length
        ): Int {
            for (i in startIndex until endIndex) {
                when (this[i]) {
                    '\t', '\n', '\u000C', '\r', ' ' -> Unit
                    else -> return i
                }
            }
            return endIndex
        }

        internal fun String.indexOfLastNonAsciiWhitespace(
            startIndex: Int = 0,
            endIndex: Int = length
        ): Int {
            for (i in endIndex - 1 downTo startIndex) {
                when (this[i]) {
                    '\t', '\n', '\u000C', '\r', ' ' -> Unit
                    else -> return i + 1
                }
            }
            return startIndex
        }

        override fun toString(): String = ""
    }

    companion object {
        fun String.toHttpUrl(): HttpUrl = HttpUrl.Builder().parse(null, this).build()
    }
}