package com.amoure.amoure.data.retrofit

import com.amoure.amoure.data.request.PostCartRequest
import com.amoure.amoure.data.request.PostProductRequest
import com.amoure.amoure.data.request.PostReviewRequest
import com.amoure.amoure.data.request.PostWishlistRequest
import com.amoure.amoure.data.request.PutProfileRequest
import com.amoure.amoure.data.response.CategoryItem
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.ImageResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.LoginResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.response.Profile
import com.amoure.amoure.data.response.RentResponse
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.data.response.ReviewResponse
import com.amoure.amoure.data.response.SearchResponse
import com.amoure.amoure.data.response.VisSearchResponse
import com.amoure.amoure.data.response.WishlistResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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
    fun getProfile(): Call<InitialResponse<Profile>>

    @PATCH("auth/profile")
    fun putProfile(
        @Body profile: PutProfileRequest
    ): Call<InitialResponse<IdResponse>>

    @GET("products")
    suspend fun getProducts(
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
    ): Call<InitialResponse<ProductItem>>

    @POST("orders")
    fun postToCart(
        @Body body: PostCartRequest
    ): Call<InitialResponse<IdResponse>>

    @GET("ml/search")
    fun searchByQuery(
        @Query("query") name: String
    ): Call<InitialResponse<SearchResponse>>

    @GET("reviews")
    suspend fun getReviews(
        @Query("id") id: Int? = null,
        @Query("productId") productId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("take") take: Int? = null,
        @Query("includeUser") includeUser: Boolean = true,
    ): InitialResponse<ReviewResponse>

    @POST("reviews")
    fun postReview(
        @Body review: PostReviewRequest,
    ): Call<InitialResponse<ReviewItem>>

    @DELETE("reviews/{productId}")
    fun deleteReview(
        @Path("productId") productId: Int
    ): Call<InitialResponse<IdResponse>>

    @GET("orders")
    suspend fun getRents(
        @Query("userId") userId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("take") take: Int? = null,
        @Query("includeProduct") includeProduct: Boolean = true,
    ): InitialResponse<RentResponse>

    @POST("wishlist")
    fun postWishlist(
        @Body body: PostWishlistRequest
    ): Call<InitialResponse<IdResponse>>

    @GET("wishlist")
    fun getUserWishlist(
        @Query("includeProducts") includeProducts: Boolean = true,
    ): Call<InitialResponse<WishlistResponse>>

    @POST("products")
    fun postProduct(
        @Body product: PostProductRequest
    ): Call<InitialResponse<ProductItem>>

    @Multipart
    @POST("products/image/upload")
    fun postProductImage(
        @Part file: MultipartBody.Part,
    ): Call<InitialResponse<ImageResponse>>

    @GET("ml/image/search")
    fun searchByImage(
        @Query("imageUrl") name: String
    ): Call<InitialResponse<VisSearchResponse>>

    @GET("ml/vton")
    fun tryOn(
        @Query("vtonUrl") vtonUrl: String,
        @Query("productId") productId: Int,
        @Query("category") category: String,
    ): Call<InitialResponse<ImageResponse>>

    @GET("categories")
    fun getAllCategory(): Call<InitialResponse<List<CategoryItem>>>
    @Multipart
    @POST("ml/image/upload")
    fun postMlImage(
        @Part file: MultipartBody.Part,
    ): Call<InitialResponse<ImageResponse>>
}