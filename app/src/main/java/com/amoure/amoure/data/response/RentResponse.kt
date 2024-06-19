package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class RentResponse(

	@field:SerializedName("orders")
	val orders: List<RentItem?>? = null
)

data class RentItem(
	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("userId")
	val userId: Int? = null,

	@SerializedName("productId")
	val productId: Int? = null,

	@SerializedName("productName")
	val productName: String? = null,

	@SerializedName("productSize")
	val productSize: String? = null,

	@SerializedName("productColor")
	val productColor: String? = null,

	@SerializedName("rentPrice")
	val rentPrice: Int? = null,

	@SerializedName("deliveryMethod")
	val deliveryMethod: String? = null,

	@SerializedName("deliveryPrice")
	val deliveryPrice: Int? = null,

	@SerializedName("totalPrice")
	val totalPrice: Int? = null,

	@SerializedName("rentalStartDate")
	val rentalStartDate: String? = null,

	@SerializedName("rentalEndDate")
	val rentalEndDate: String? = null,

	@SerializedName("rentalDuration")
	val rentalDuration: Int? = null,

	@SerializedName("status")
	val status: String? = null,

	@SerializedName("product")
	val product: ProductItem? = null,
)
