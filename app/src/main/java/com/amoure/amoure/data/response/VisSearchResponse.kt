package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class VisSearchResponse(

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("similarProducts")
    val similarProducts: ProductsResponse? = null,
)