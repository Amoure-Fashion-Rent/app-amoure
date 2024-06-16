package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class ProductsResponse(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("products")
	val products: List<ProductItem?>? = null
)

data class ProductResponse(

	@field:SerializedName("product")
	val product: ProductItem? = null
)

data class ProductItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("stylishNotes")
	val stylishNotes: String? = null,

	@field:SerializedName("retailPrice")
	val retailPrice: Int? = null,

	@field:SerializedName("rentPrice")
	val rentPrice: Int? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("category")
	val category: Category? = null,

	@field:SerializedName("owner")
	val owner: Owner? = null,

	@field:SerializedName("avgRating")
	val avgRating: Double? = null,

	@field:SerializedName("reviewsCount")
	val reviewsCount: Int? = null,
)

data class Owner(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null
)

data class Category(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)
