package com.am.lapcart

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL =
        "https://fix-buddy.chalokhele.in/"

    fun getApi(): ApiInterface {
         return Retrofit.Builder()

            .baseUrl(BASE_URL)

            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
             .create(ApiInterface::class.java)

    }
}
