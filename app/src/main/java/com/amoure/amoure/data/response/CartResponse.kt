package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class CartResponse(

	@field:SerializedName("carts")
	val carts: Cart? = null
)

data class Cart(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("product")
	val product: ProductItem? = null,
)
