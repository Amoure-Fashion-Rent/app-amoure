package com.amoure.amoure.data

data class Repository(
    val userRepository: UserRepository,
    val productRepository: ProductRepository,
    val reviewRepository: ReviewRepository,
    val rentHistoryRepository: RentHistoryRepository,
)
