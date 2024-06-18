package com.amoure.amoure.data.retrofit

import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.LoginResponse
import com.amoure.amoure.data.response.ProductResponse
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.response.ProfileResponse
import com.amoure.amoure.data.response.RentResponse
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.data.response.ReviewResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") userType: String
    ): Call<InitialResponse<IdResponse>>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<InitialResponse<LoginResponse>>

    @DELETE("auth/logout")
    fun logout(): Call<InitialResponse<IdResponse>>

    @GET("auth/profile")
    fun getProfile(): Call<InitialResponse<ProfileResponse>>

    @PATCH("auth/profile")
    fun putProfile(
        @Field("fullName") fullName: String,
        @Field("addressDetail") addressDetail: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("postalCode") postalCode: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("birthDate") birthDate: String?,
    ): Call<InitialResponse<IdResponse>>

    @GET("products")
    fun getProducts(
        @Query("id") id: Int? = null,
        @Query("ownerId") ownerId: Int? = null,
        @Query("categoryId") categoryId: Int? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int? = null,
        @Query("take") take: Int? = null,
        @Query("includeCategory") includeCategory: Boolean = true,
        @Query("includeOwner") includeOwner: Boolean = true,
    ): InitialResponse<ProductsResponse>

    @GET("products/{productId}")
    fun getProductById(
        @Path("productId") id: Int
    ): Call<InitialResponse<ProductResponse>>

    @POST("orders")
    fun postToCart(
        @Field("productId") productId: Int,
        @Field("rentalStartDate") rentalStartDate: String,
        @Field("rentalEndDate") rentalEndDate: String,
    ): Call<InitialResponse<IdResponse>>

    @GET("products/search")
    fun getSearch(
        @Query("name") name: String
    ): Call<InitialResponse<ProductsResponse>>

    @GET("reviews")
    fun getReviews(
        @Query("id") id: Int? = null,
        @Query("productId") productId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("take") take: Int? = null,
        @Query("includeUser") includeUser: Boolean = true,
    ): InitialResponse<ReviewResponse>

    @POST("reviews")
    fun postReview(
        @Field("productId") productId: Int,
        @Field("rating") rating: Int,
        @Field("comment") comment: String,
    ): Call<InitialResponse<ReviewItem>>

    @DELETE("reviews/{productId}")
    fun deleteReview(
        @Path("productId") productId: Int
    ): Call<InitialResponse<IdResponse>>

    @GET("orders")
    fun getRents(
        @Query("userId") userId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("take") take: Int? = null,
        @Query("includeProduct") includeProduct: Boolean = true,
    ): InitialResponse<RentResponse>

    @POST("wishlist")
    fun postWishlist(
        @Field("productId") productId: Int,
    ): Call<InitialResponse<IdResponse>>
}