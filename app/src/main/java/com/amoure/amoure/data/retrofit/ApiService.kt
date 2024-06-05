package com.amoure.amoure.data.retrofit

import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.LoginResponse
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("userType") userType: String
    ): Call<InitialResponse<RegisterResponse>>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<InitialResponse<LoginResponse>>

    @GET("products")
    fun getProducts(): Call<InitialResponse<ProductsResponse>>

    @GET("products/{productId}")
    fun getProductById(
        @Path("productId") id: String
    ): Call<InitialResponse<ProductsResponse>>

    // TODO: Change to CartResponse
    @GET("/carts/{userId}")
    fun getUserCart(
        @Path("userId") id: String
    ): Call<InitialResponse<ProductsResponse>>

    @PUT("/carts/{userId}")
    fun putFromCart(
        @Path("userId") id: String,
        @Field("delivery") delivery: String,
        @Field("deliveryPrice") deliveryPrice: Int,
        @Field("cardNumber") cardNumber: String,
        @Field("cardCVV") cardCVV: String,
        @Field("cardExpiry") cardExpiry: String,
    ): Call<InitialResponse<IdResponse>>

    @POST("/carts/{userId}/{productId}")
    fun postToCart(
        @Path("userId") userId: String,
        @Path("productId") productId: String
    ): Call<InitialResponse<IdResponse>>

    @DELETE("/carts/{userId}/{productId}")
    fun deleteFromCart(
        @Path("userId") userId: String,
        @Path("productId") productId: String
    ): Call<InitialResponse<IdResponse>>

    @GET("products/search")
    fun getSearch(
        @Query("name") name: String
    ): Call<InitialResponse<ProductsResponse>>
}