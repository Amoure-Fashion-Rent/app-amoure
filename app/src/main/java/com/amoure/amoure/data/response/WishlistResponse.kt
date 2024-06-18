package com.amoure.amoure.data.response


import com.google.gson.annotations.SerializedName

data class WishlistResponse(

    @field:SerializedName("cart")
    val wishlist: Wishlist? = null
)

data class Wishlist(
    @field:SerializedName("productId")
    val productId: List<String>? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

)
