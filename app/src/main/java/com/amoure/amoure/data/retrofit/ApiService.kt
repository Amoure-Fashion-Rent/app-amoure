package com.amoure.amoure.data.retrofit

import com.amoure.amoure.data.response.CartResponse
import com.amoure.amoure.data.response.CategoryResponse
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.LoginResponse
import com.amoure.amoure.data.response.ProductResponse
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.response.WishlistResponse
import com.amoure.amoure.data.response.ProfileResponse
import com.amoure.amoure.data.response.RentResponse
import com.amoure.amoure.data.response.ReviewResponse
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
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("userType") userType: String
    ): Call<InitialResponse<IdResponse>>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<InitialResponse<LoginResponse>>

    @GET("users/{userId}")
    fun getProfile(
        @Path("userId") userId: String,
    ): Call<InitialResponse<ProfileResponse>>

    @PUT("users/{userId}")
    fun putProfile(
        @Path("userId") userId: String,
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("addressDetail") addressDetail: String,
        @Field("province") province: String,
        @Field("city") city: String,
        @Field("district") district: String,
        @Field("postalCode") postalCode: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("birthDate") birthDate: String?,
    ): Call<InitialResponse<IdResponse>>

    @GET("products")
    fun getProducts(): Call<InitialResponse<ProductsResponse>>

    @GET("products/{ownerId}/owner")
    fun getProductsByOwner(
        @Path("ownerId") ownerId: String
    ): Call<InitialResponse<ProductsResponse>>

    @GET("products/{productId}")
    fun getProductById(
        @Path("productId") id: String
    ): Call<InitialResponse<ProductResponse>>

    @GET("/carts/{userId}")
    fun getUserCart(
        @Path("userId") id: String
    ): Call<InitialResponse<CartResponse>>

    @PUT("/carts/{userId}")
    fun putFromCart(
        @Path("userId") id: String,
        @Field("rentalStartDate") rentalStartDate: String,
        @Field("rentalEndDate") rentalEndDate: String,
        @Field("rentalDuration") rentalDuration: Int,
        @Field("delivery") delivery: String,
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

    @GET("reviews/{productId}")
    fun getReviews(
    @Path("productId") productId: String
    ): Call<InitialResponse<ReviewResponse>>

    @POST("reviews/{userId}/{productId}")
    fun postReview(
        @Path("userId") userId: String,
        @Path("productId") productId: String,
        @Field("rating") rating: Int,
        @Field("comment") comment: String,
        @Field("createdAt") createdAt: String,
    ): Call<InitialResponse<IdResponse>>

    @DELETE("reviews/{userId}/{productId}")
    fun deleteReview(
        @Path("userId") userId: String,
        @Path("productId") productId: String
    ): Call<InitialResponse<IdResponse>>

    @GET("rents/{userId}")
    fun getRents(
        @Path("userId") userId: String
    ): Call<InitialResponse<RentResponse>>

    @GET("/wishlists/{userId}")
    fun getUserWishlist(
        @Path("userId") id: String
    ): Call<InitialResponse<WishlistResponse>>

    @POST("/wishlists/{userId}/{productId}")
    fun postToWishlist(
        @Path("userId") userId: String,
        @Path("productId") productId: String
    ): Call<InitialResponse<IdResponse>>

    @DELETE("/wishlists/{userId}/{productId}")
    fun deleteFromWishlist(
        @Path("userId") userId: String,
        @Path("productId") productId: String
    ): Call<InitialResponse<IdResponse>>

    @POST("users/{ownerId}")
    fun postProduct(
        @Path("ownerId") ownerId: String,
        @Field("name") name: String,
        @Field("product") product: String,
        @Field("details") details: String,
        @Field("notes") notes: String,
        @Field("retail") retail: String,
        @Field("rent") rent: String,
        @Field("category") category: String,
        @Field("sizes") sizes: String,
        @Field("images") images: String,
    ): Call<InitialResponse<IdResponse>>

    /////get search by vissearch
    @GET("products/search")
    fun getSearchbyVisSearch(
        @Query("name") name: String
    ): Call<InitialResponse<ProductsResponse>>

    @GET("products/category")
    fun getAllCategory(
        @Query("name") name: String
    ): Call<InitialResponse<CategoryResponse>>

    @GET("products/{categoryId}")
    fun getAllCategorybyId(
        @Query("id") id: String
    ): Call<InitialResponse<CategoryResponse>>

}