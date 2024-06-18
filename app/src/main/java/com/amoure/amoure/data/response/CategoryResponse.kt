package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(

    @field:SerializedName("products")
    val products: List<CategoryItem?>? = null
)

data class CategoryResponse(

    @field:SerializedName("product")
    val product: List<CategoryItem?>? = null
)

data class CategoryItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("categoriesName")
    val categoriesName: List<String?>? = null,

//    @field:SerializedName("imgProduct")
//    val imgProduct: List<String?>? = null,
)