package eu.kanade.tachiyomi.network

//import android.content.Context
import okhttp3.OkHttpClient
import kotlin.time.DurationUnit

class NetworkHelper { //(context: Context) {

//    private val userAgentInterceptor by lazy { UserAgentInterceptor() }
//    private val cloudflareInterceptor by lazy { CloudflareInterceptor(context) }

    private val baseClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient.Builder()
//                .cookieJar(cookieManager)
                .connectTimeout(30, DurationUnit.SECONDS)
                .readTimeout(30, DurationUnit.SECONDS)
                .callTimeout(2, DurationUnit.MINUTES)
//                .addInterceptor(userAgentInterceptor)

    val client by lazy { baseClientBuilder.build() }

    val cloudflareClient by lazy {
        client.newBuilder()
//            .addInterceptor(cloudflareInterceptor)
            .build()
    }

    val defaultUserAgent = "Tachiyomi"
}