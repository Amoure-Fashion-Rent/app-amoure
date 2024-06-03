package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class ProductsResponse(

	@field:SerializedName("products")
	val products: List<ProductItem?>? = null
)

data class ProductResponse(

	@field:SerializedName("products")
	val product: ProductItem? = null
)

data class ProductItem(

	@field:SerializedName("size")
	val size: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("rentPrice")
	val rentPrice: Int? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("ownerId")
	val ownerId: String? = null,

	// TODO
	@field:SerializedName("ownerName")
	val ownerName: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("retailPrice")
	val retailPrice: Int? = null,

	@field:SerializedName("imgProduct")
	val imgProduct: String? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null
)
