package com.kirwa.taskapp.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kirwa.taskapp.data.remote.model.Exclude
import com.kirwa.taskapp.utils.DateDeserializer
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

/**
 * createNetworkClient
 */
fun createNetworkClient(
    baseUrl: String,
    apiToken: String
) =
    retrofitClient(
        httpClient(apiToken),
        baseUrl,
        provideGson()
    )


private class BasicAuthInterceptor(val apiToken: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        appendHeaders(requestBuilder, apiToken)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

}

/**
 * httpClient
 */
fun httpClient(apiToken: String): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    val clientBuilder = OkHttpClient.Builder()
   // if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(httpLoggingInterceptor)
   // }
    clientBuilder.addInterceptor(BasicAuthInterceptor(apiToken))
    clientBuilder.readTimeout(120, TimeUnit.SECONDS)
    clientBuilder.writeTimeout(120, TimeUnit.SECONDS)

    return clientBuilder.build()
}


private fun retrofitClient(okHttpClient: OkHttpClient, baseUrl: String, gson: Gson): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()




private fun appendHeaders(requestBuilder: Request.Builder, apiToken: String) {
    try {
            requestBuilder.header(
                "Authorization",
                "Bearer $apiToken"
            )
    } catch (e: IOException) {
        e.printStackTrace()
    }

}

/**
 * provideGson
 */
fun provideGson(): Gson {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.addSerializationExclusionStrategy(Exclude())
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    gsonBuilder.registerTypeAdapter(
        Date::class.java,
        DateDeserializer()
    )
    return gsonBuilder.create()
}

