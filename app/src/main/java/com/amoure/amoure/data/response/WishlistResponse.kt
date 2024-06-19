package com.amoure.amoure.data.response


import com.google.gson.annotations.SerializedName

data class WishlistResponse(

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("wishlist")
    val wishlist: List<WishlistItem?>? = null
)

data class WishlistItem(

    @field:SerializedName("productId")
    val productId: Int? = null,

    @field:SerializedName("userId")
    val userId: Int? = null,

    @field:SerializedName("product")
    val product: ProductItem? = null
)
