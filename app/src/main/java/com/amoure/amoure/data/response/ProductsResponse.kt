package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class ProductsResponse(

	@field:SerializedName("products")
	val products: List<ProductItem?>? = null
)

data class ProductResponse(

	@field:SerializedName("product")
	val product: ProductItem? = null
)

data class ProductItem(

	@field:SerializedName("ownerName")
	val ownerName: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("styleNotes")
	val styleNotes: String? = null,

	@field:SerializedName("available")
	val available: Boolean? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("rentPrice")
	val rentPrice: Int? = null,

	@field:SerializedName("retailPrice")
	val retailPrice: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("productName")
	val productName: String? = null,

	@field:SerializedName("imgProduct")
	val imgProduct: List<String?>? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null
)
