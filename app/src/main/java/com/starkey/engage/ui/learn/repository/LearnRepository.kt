package com.starkey.engage.ui.learn.repository

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LearnRepository {
    var learnService: LearnService

    companion object {
        private const val BASE_URL = "https://www.sitecorepoc.com/"
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        val client: OkHttpClient = Builder().addInterceptor(interceptor).build()
        learnService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LearnService::class.java)

//         .client(client)
    }
}