package okhttp3

import okio.Buffer

class HttpUrl internal constructor(
    val scheme: String,
    val pathSegments: List<String>,
    private val url: String
) {
    val isHttps: Boolean
        get() = scheme == "https"

    override fun toString(): String = url

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
            encodedPathSegments,
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
            val slashCount = input.slashCount(pos, limit)
            if (slashCount >= 2 || base == null || base.scheme != this.scheme) {
//                // Read an authority if either:
//                //  * The input starts with 2 or more slashes. These follow the scheme if it exists.
//                //  * The input scheme exists and is different from the base URL's scheme.
//                //
//                // The structure of an authority is:
//                //   username:password@host:port
//                //
//                // Username, password and port are optional.
//                //   [username[:password]@]host[:port]
                pos += slashCount
                authority@ while (true) {
                    val componentDelimiterOffset = input.delimiterOffset("@/\\?#", pos, limit)
                    val c = if (componentDelimiterOffset != limit) {
                        input[componentDelimiterOffset].code
                    } else {
                        -1
                    }
                    when (c) {
                        '@'.code -> {
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
                        }
//
                        -1, '/'.code, '\\'.code, '?'.code, '#'.code -> {
//                            // Host info precedes.
                            val portColonOffset = portColonOffset(input, pos, componentDelimiterOffset)
                            if (portColonOffset + 1 < componentDelimiterOffset) {
                                host = input.percentDecode(pos = pos, limit = portColonOffset).toCanonicalHost()
//                                port = parsePort(input, portColonOffset + 1, componentDelimiterOffset)
//                                require(port != -1) {
//                                    "Invalid URL port: \"${input.substring(portColonOffset + 1,
//                                        componentDelimiterOffset)}\""
//                                }
                            } else {
                                host = input.percentDecode(pos = pos, limit = portColonOffset).toCanonicalHost()
                                port = HttpUrl.defaultPort(scheme!!)
                            }
                            require(host != null) {
                                "invalid host ${input.substring(pos, portColonOffset)}"
                            }
//                            require(host != null) {
//                                "$INVALID_HOST: \"${input.substring(pos, portColonOffset)}\""
//                            }
                            pos = componentDelimiterOffset
                            break@authority
                        }
                    }
                }
            } else {
//                // This is a relative link. Copy over all authority components. Also maybe the path & query.
//                this.encodedUsername = base.encodedUsername
//                this.encodedPassword = base.encodedPassword
//                this.host = base.host
//                this.port = base.port
                this.encodedPathSegments.clear()
//                this.encodedPathSegments.addAll(base.encodedPathSegments)
//                if (pos == limit || input[pos] == '#') {
//                    encodedQuery(base.encodedQuery)
//                }
            }
//
//            // Resolve the relative path.
            val pathDelimiterOffset = input.delimiterOffset("?#", pos, limit)
            resolvePath(input, pos, pathDelimiterOffset)
            pos = pathDelimiterOffset

            // Query.
            if (pos < limit && input[pos] == '?') {
                val queryDelimiterOffset = input.delimiterOffset('#', pos, limit)
//                this.encodedQueryNamesAndValues = input.canonicalize(
//                    pos = pos + 1,
//                    limit = queryDelimiterOffset,
//                    encodeSet = QUERY_ENCODE_SET,
//                    alreadyEncoded = true,
//                    plusIsSpace = true
//                ).toQueryNamesAndValues()
                pos = queryDelimiterOffset
            }

            // Fragment.
            if (pos < limit && input[pos] == '#') {
//                this.encodedFragment = input.canonicalize(
//                    pos = pos + 1,
//                    limit = limit,
//                    encodeSet = FRAGMENT_ENCODE_SET,
//                    alreadyEncoded = true,
//                    unicodeAllowed = true
//                )
            }

            return this
        }

        fun String.isPercentEncoded(pos: Int, limit: Int): Boolean {
            return pos + 2 < limit &&
                    this[pos] == '%' &&
                    this[pos + 1].parseHexDigit() != -1 &&
                    this[pos + 2].parseHexDigit() != -1
        }

        fun Buffer.writeCanonicalized(
            input: String,
            pos: Int,
            limit: Int,
            encodeSet: String,
            alreadyEncoded: Boolean,
            strict: Boolean,
            plusIsSpace: Boolean,
            unicodeAllowed: Boolean,
        ) {
            var encodedCharBuffer: Buffer? = null // Lazily allocated.
            var codePoint: Int
            var i = pos
            while (i < limit) {
                codePoint = input[i].code
                if (alreadyEncoded && (codePoint == '\t'.code || codePoint == '\n'.code ||
                            codePoint == '\u000c'.code || codePoint == '\r'.code)) {
                    // Skip this character.
                } else if (codePoint == ' '.code && encodeSet === " !\"#$&'()+,/:;<=>?@[\\]^`{|}~") {
                    // Encode ' ' as '+'.
                    writeUtf8("+")
                } else if (codePoint == '+'.code && plusIsSpace) {
                    // Encode '+' as '%2B' since we permit ' ' to be encoded as either '+' or '%20'.
                    writeUtf8(if (alreadyEncoded) "+" else "%2B")
                } else if (codePoint < 0x20 ||
                    codePoint == 0x7f ||
                    codePoint >= 0x80 && !unicodeAllowed ||
                    codePoint.toChar() in encodeSet ||
                    codePoint == '%'.code &&
                    (!alreadyEncoded || strict && !input.isPercentEncoded(i, limit))) {
                    // Percent encode this character.
                    if (encodedCharBuffer == null) {
                        encodedCharBuffer = Buffer()
                    }

                    encodedCharBuffer.writeUtf8CodePoint(codePoint)

                    val HEX_DIGITS =
                        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

                    while (!encodedCharBuffer.exhausted()) {
                        val b = encodedCharBuffer.readByte().toInt() and 0xff
                        writeByte('%'.code)
                        writeByte(HEX_DIGITS[b shr 4 and 0xf].code)
                        writeByte(HEX_DIGITS[b and 0xf].code)
                    }
                } else {
                    // This character doesn't need encoding. Just copy it over.
                    writeUtf8CodePoint(codePoint)
                }
                i += 1
            }
        }

        fun String.canonicalize(
            pos: Int = 0,
            limit: Int = length,
            encodeSet: String,
            alreadyEncoded: Boolean = false,
            strict: Boolean = false,
            plusIsSpace: Boolean = false,
            unicodeAllowed: Boolean = false,
        ): String {
            var codePoint: Int
            var i = pos
            while (i < limit) {
                codePoint = this[i].code
                if (codePoint < 0x20 ||
                    codePoint == 0x7f ||
                    codePoint >= 0x80 && !unicodeAllowed ||
                    codePoint.toChar() in encodeSet ||
                    codePoint == '%'.code &&
                    (!alreadyEncoded || strict && !isPercentEncoded(i, limit)) ||
                    codePoint == '+'.code && plusIsSpace
                ) {
                    // Slow path: the character at i requires encoding!
                    val out = Buffer()
                    out.writeUtf8(this, pos, i)
                    out.writeCanonicalized(
                        input = this,
                        pos = i,
                        limit = limit,
                        encodeSet = encodeSet,
                        alreadyEncoded = alreadyEncoded,
                        strict = strict,
                        plusIsSpace = plusIsSpace,
                        unicodeAllowed = unicodeAllowed,
                    )
                    return out.readUtf8()
                }
                i += 1
            }

            // Fast path: no characters in [pos..limit) required encoding.
            return substring(pos, limit)
        }

        fun push(
            input: String,
            pos: Int,
            limit: Int,
            addTrailingSlash: Boolean,
            alreadyEncoded: Boolean
        ) {
            val segment = input.canonicalize(
                pos = pos,
                limit = limit,
                encodeSet = " \"<>^`{}|/\\?#", // PATH_SEGMENT_ENCODE_SET,
                alreadyEncoded = alreadyEncoded
            )
            if (isDot(segment)) {
                return // Skip '.' path segments.
            }
            if (isDotDot(segment)) {
                pop()
                return
            }
            if (encodedPathSegments[encodedPathSegments.size - 1].isEmpty()) {
                encodedPathSegments[encodedPathSegments.size - 1] = segment
            } else {
                encodedPathSegments.add(segment)
            }
            if (addTrailingSlash) {
                encodedPathSegments.add("")
            }
        }

        fun isDot(input: String): Boolean {
            return input == "." || input.equals("%2e", ignoreCase = true)
        }

        fun isDotDot(input: String): Boolean {
            return input == ".." ||
                    input.equals("%2e.", ignoreCase = true) ||
                    input.equals(".%2e", ignoreCase = true) ||
                    input.equals("%2e%2e", ignoreCase = true)
        }

        fun pop() {
            val removed = encodedPathSegments.removeAt(encodedPathSegments.size - 1)

            // Make sure the path ends with a '/' by either adding an empty string or clearing a segment.
            if (removed.isEmpty() && encodedPathSegments.isNotEmpty()) {
                encodedPathSegments[encodedPathSegments.size - 1] = ""
            } else {
                encodedPathSegments.add("")
            }
        }

        fun resolvePath(input: String, startPos: Int, limit: Int) {
            var pos = startPos
            // Read a delimiter.
            if (pos == limit) {
                // Empty path: keep the base path as-is.
                return
            }
            val c = input[pos]
            if (c == '/' || c == '\\') {
                // Absolute path: reset to the default "/".
                encodedPathSegments.clear()
                encodedPathSegments.add("")
                pos++
            } else {
                // Relative path: clear everything after the last '/'.
                encodedPathSegments[encodedPathSegments.size - 1] = ""
            }

            // Read path segments.
            var i = pos
            while (i < limit) {
                val pathSegmentDelimiterOffset = input.delimiterOffset("/\\", i, limit)
                val segmentHasTrailingSlash = pathSegmentDelimiterOffset < limit
                push(input, i, pathSegmentDelimiterOffset, segmentHasTrailingSlash, true)
                i = pathSegmentDelimiterOffset
                if (segmentHasTrailingSlash) i++
            }
        }

        fun String.containsInvalidHostnameAsciiCodes(): Boolean {
            for (i in 0 until length) {
                val c = this[i]
                // The WHATWG Host parsing rules accepts some character codes which are invalid by
                // definition for OkHttp's host header checks (and the WHATWG Host syntax definition). Here
                // we rule out characters that would cause problems in host headers.
                if (c <= '\u001f' || c >= '\u007f') {
                    return true
                }
                // Check for the characters mentioned in the WHATWG Host parsing spec:
                // U+0000, U+0009, U+000A, U+000D, U+0020, "#", "%", "/", ":", "?", "@", "[", "\", and "]"
                // (excluding the characters covered above).
                if (" #%/:?@[\\]".indexOf(c) != -1) {
                    return true
                }
            }
            return false
        }

        fun String.containsInvalidLabelLengths(): Boolean {
            if (length !in 1..253) return true

            var labelStart = 0
            while (true) {
                val dot = indexOf('.', startIndex = labelStart)
                val labelLength = when (dot) {
                    -1 -> length - labelStart
                    else -> dot - labelStart
                }
                if (labelLength !in 1..63) return true
                if (dot == -1) break
                if (dot == length - 1) break // Trailing '.' is allowed.
                labelStart = dot + 1
            }

            return false
        }

        fun String.toCanonicalHost(): String? {
            val host: String = this

            // If the input contains a :, it’s an IPv6 address.
//            if (":" in host) {
//                // If the input is encased in square braces "[...]", drop 'em.
//                val inetAddressByteArray = (if (host.startsWith("[") && host.endsWith("]")) {
//                    decodeIpv6(host, 1, host.length - 1)
//                } else {
//                    decodeIpv6(host, 0, host.length)
//                }) ?: return null
//                // TODO implement properly
//                return inet6AddressToAscii(inetAddressByteArray)
//            }

            try {
                val result = host.lowercase()
                if (result.isEmpty()) return null

                return if (result.containsInvalidHostnameAsciiCodes()) {
                    // The IDN ToASCII result contains illegal characters.
                    null
                } else if (result.containsInvalidLabelLengths()) {
                    // The IDN ToASCII result contains invalid labels.
                    null
                } else {
                    result
                }
            } catch (_: IllegalArgumentException) {
                return null
            }
        }

        fun Char.parseHexDigit(): Int = when (this) {
            in '0'..'9' -> this - '0'
            in 'a'..'f' -> this - 'a' + 10
            in 'A'..'F' -> this - 'A' + 10
            else -> -1
        }

        fun Buffer.writePercentDecoded(
            encoded: String,
            pos: Int,
            limit: Int,
            plusIsSpace: Boolean
        ) {
            var codePoint: Int
            var i = pos
            while (i < limit) {
                codePoint = encoded.get(i).code
                if (codePoint == '%'.code && i + 2 < limit) {
                    val d1 = encoded[i + 1].parseHexDigit()
                    val d2 = encoded[i + 2].parseHexDigit()
                    if (d1 != -1 && d2 != -1) {
                        writeByte((d1 shl 4) + d2)
                        i += 2
                        i += 1
                        continue
                    }
                } else if (codePoint == '+'.code && plusIsSpace) {
                    writeByte(' '.code)
                    i++
                    continue
                }
                writeUtf8CodePoint(codePoint)
                i += 1
            }
        }

        fun String.percentDecode(
            pos: Int = 0,
            limit: Int = length,
            plusIsSpace: Boolean = false
        ): String {
            for (i in pos until limit) {
                val c = this[i]
                if (c == '%' || c == '+' && plusIsSpace) {
                    // Slow path: the character at i requires decoding!
                    val out = Buffer()
                    out.writeUtf8(this, pos, i)
                    out.writePercentDecoded(this, pos = i, limit = limit, plusIsSpace = plusIsSpace)
                    return out.readUtf8()
                }
            }

            // Fast path: no characters in [pos..limit) required decoding.
            return substring(pos, limit)
        }

        fun port(port: Int) = apply {
            require(port in 1..65535) { "unexpected port: $port" }
            this.port = port
        }

        fun String.delimiterOffset(
            delimiters: String,
            startIndex: Int = 0,
            endIndex: Int = length
        ): Int {
            for (i in startIndex until endIndex) {
                if (this[i] in delimiters) return i
            }
            return endIndex
        }

        fun String.delimiterOffset(
            delimiter: Char,
            startIndex: Int = 0,
            endIndex: Int = length
        ): Int {
            for (i in startIndex until endIndex) {
                if (this[i] == delimiter) return i
            }
            return endIndex
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

        override fun toString(): String {
            return buildString {
                if (scheme != null) {
                    append(scheme)
                    append("://")
                } else {
                    append("//")
                }

                if (host != null) {
                    if (':' in host!!) {
                        // Host is an IPv6 address.
                        append('[')
                        append(host)
                        append(']')
                    } else {
                        append(host)
                    }
                }

                encodedPathSegments.toPathString(this)

                if (encodedQueryNamesAndValues != null) {
                    append('?')
                    encodedQueryNamesAndValues!!.toQueryString(this)
                }
            }
        }

        private fun List<String>.toPathString(out: StringBuilder) {
            for (element in this) {
                out.append('/')
                out.append(element)
            }
        }

        private fun List<String?>.toQueryString(out: StringBuilder) {
            for (i in indices step 2) {
                val name = this[i]
                val value = this[i + 1]
                if (i > 0) out.append('&')
                out.append(name)
                if (value != null) {
                    out.append('=')
                    out.append(value)
                }
            }
        }
    }

    companion object {
        fun String.toHttpUrl(): HttpUrl = HttpUrl.Builder().parse(null, this).build()

        fun defaultPort(scheme: String): Int {
            return when (scheme) {
                "http" -> 80
                "https" -> 443
                else -> -1
            }
        }
    }
}