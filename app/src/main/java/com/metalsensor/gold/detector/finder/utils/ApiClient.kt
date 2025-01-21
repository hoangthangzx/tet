// ApiClient.kt
package com.metalsensor.gold.detector.finder.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://qmegas.info/numista-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
