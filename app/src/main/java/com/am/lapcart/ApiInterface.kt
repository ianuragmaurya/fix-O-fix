package com.am.lapcart

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("api/customers/login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>


    @POST("api/customers/register")
    fun registerUser(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>


    @GET("api/categories")
    fun getCategories(): Call<CategoryResponse>


    @GET("api/services")
    fun getServices(
        @Query("category_id") Id: Int
    ): Call<ServiceResponse>

}
