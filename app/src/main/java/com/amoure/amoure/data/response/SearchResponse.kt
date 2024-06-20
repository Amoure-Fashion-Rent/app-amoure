package com.amoure.amoure.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

    @field:SerializedName("products")
    val products: ProductsResponse? = null,
)