package com.amoure.amoure.data.retrofit

import com.amoure.amoure.data.response.LoginResponse
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("userType") userType: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("products")
    fun getProducts(): Call<ProductsResponse>

    @GET("products/{productId}")
    fun getProductById(
        @Path("productId") id: String
    ): Call<ProductsResponse>
}