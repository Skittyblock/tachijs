package okhttp3

class MediaType internal constructor(
    internal val mediaType: String,
    val type: String,
    val subtype: String,
    internal val parameterNamesAndValues: Array<String>
) {

//    fun charset(defaultValue: Charset? = null): Charset? {
//        val charset = parameter("charset") ?: return defaultValue
//        return try {
//            Charset.forName(charset)
//        } catch (_: IllegalArgumentException) {
//            defaultValue // This charset is invalid or unsupported. Give up.
//        }
//    }

    companion object {
//        fun String.toMediaType(): MediaType = commonToMediaType()
//        fun String.toMediaTypeOrNull(): MediaType? = commonToMediaTypeOrNull()
    }
}