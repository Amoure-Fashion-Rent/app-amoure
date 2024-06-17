package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class RentResponse(

	@field:SerializedName("orders")
	val orders: List<RentItem?>? = null
)

data class RentItem(
	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("user_id")
	val userId: Int? = null,

	@SerializedName("product_id")
	val productId: Int? = null,

	@SerializedName("product_name")
	val productName: String? = null,

	@SerializedName("size")
	val productSize: String? = null,

	@SerializedName("color")
	val productColor: String? = null,

	@SerializedName("rent_price")
	val rentPrice: Int? = null,

	@SerializedName("delivery_method")
	val deliveryMethod: String? = null,

	@SerializedName("delivery_price")
	val deliveryPrice: Int? = null,

	@SerializedName("total_price")
	val totalPrice: Int? = null,

	@SerializedName("rental_start_date")
	val rentalStartDate: String? = null,

	@SerializedName("rental_end_date")
	val rentalEndDate: String? = null,

	@SerializedName("rental_duration")
	val rentalDuration: Int? = null,

	@SerializedName("status")
	val status: String? = null,

	@SerializedName("product")
	val product: ProductItem? = null,
)
