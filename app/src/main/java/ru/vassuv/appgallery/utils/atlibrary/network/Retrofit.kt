package ru.vassuv.appgallery.utils.atlibrary.network

import kotlinx.coroutines.experimental.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import ru.vassuv.appgallery.utils.SharedData
import ru.vassuv.appgallery.utils.atlibrary.Logger
import ru.vassuv.appgallery.utils.atlibrary.getString
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun Call<String>.await(): String = withTimeoutOrNull(10000) {
    suspendCoroutine<String> { continuation ->
        enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                t?.let { continuation.resumeWithException(it) }
                        ?: continuation.resumeWithException(ERROR_EMPTY)
            }

            override fun onResponse(call: Call<String>?, response: retrofit2.Response<String>?) {
                response?.body()?.let { continuation.resume(it) }
                        ?: response?.let { continuation.resumeWithException(Error(it.code(), it.message(), it.errorBody().toString())) }
                        ?: continuation.resumeWithException(ERROR_EMPTY)
            }
        })
    }
} ?: throw ERROR_TIMEOUT

val RETROFIT: IRequest by lazy({
    retrofit2.Retrofit.Builder()
            .baseUrl(IRequest.Urls.BASE)
            .client(OkHttpClient
                    .Builder()
                    .followRedirects(true)
                    .addNetworkInterceptor {
                        val request = it.request().newBuilder()
                                .header("Content-Type","application/json")
                                .header("Authorization", SharedData.TOKEN.getString())
                                .build()
                        Logger.trace("===> SERVER", "url =", request.url())
                        Logger.trace("===> SERVER", "method =", request.method())
                        Logger.trace("===> SERVER", "headers =", request.headers()
                                .toMultimap()
                                .map { "\n            ${it.key}:${it.value.toList().first()}" }
                                .joinToString(prefix = "", postfix = ""))
                        val response = it.proceed(request)
                        Logger.trace("<<<< SERVER", response)
                        response
                    }.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(IRequest::class.java)
})

interface IRequest {
    object Urls {
        val BASE = "https://google.com"
    }

    @GET
    fun get(@Url url: String): Call<String>

    @GET
    fun get(@Url url: String, @QueryMap(encoded = true) params: Map<String, String>): Call<String>

    @POST
    @Headers("Content-Type: application/json")
    fun post(@Url url: String, @Body params: String): Call<String>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>): Call<String>
}