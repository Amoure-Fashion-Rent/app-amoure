package com.amoure.amoure.data.request

class PostProductRequest(
    var name: String,
    var images: List<String>,
    var description: String,
    var retailPrice: Int,
    var rentPrice: Int,
    var size: String,
    var color: String,
    var status: String = "AVAILABLE",
    var stylishNotes: String,
    var categoryId: Int,
)