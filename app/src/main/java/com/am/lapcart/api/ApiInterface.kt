package com.am.lapcart.api

import com.am.lapcart.models.CategoryResponse
import com.am.lapcart.models.ServiceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("api/categories")
    fun getCategories(): Call<CategoryResponse>

    @GET("api/services")
    fun getServices(
        @Query("category_id") id: Int
    ): Call<ServiceResponse>

}