package com.amoure.amoure.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amoure.amoure.data.Repository
import com.amoure.amoure.di.Injection
import com.amoure.amoure.ui.cart.CartViewModel
import com.amoure.amoure.ui.designer.DesignerViewModel
import com.amoure.amoure.ui.editprofile.EditProfileViewModel
import com.amoure.amoure.ui.home.HomeViewModel
import com.amoure.amoure.ui.login.LoginViewModel
import com.amoure.amoure.ui.main.MainViewModel
import com.amoure.amoure.ui.product.ProductViewModel
import com.amoure.amoure.ui.profile.ProfileViewModel
import com.amoure.amoure.ui.renthistory.RentHistoryViewModel
import com.amoure.amoure.ui.review.ReviewViewModel
import com.amoure.amoure.ui.search.SearchViewModel

class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository.userRepository, repository.productRepository) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(repository.userRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository.userRepository, repository.productRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository.userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository.userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository.userRepository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository.userRepository) as T
            }
            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                ProductViewModel(repository.userRepository) as T
            }
            modelClass.isAssignableFrom(DesignerViewModel::class.java) -> {
                DesignerViewModel(repository.userRepository, repository.productRepository) as T
            }
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> {
                ReviewViewModel(repository.userRepository, repository.reviewRepository) as T
            }
            modelClass.isAssignableFrom(RentHistoryViewModel::class.java) -> {
                RentHistoryViewModel(repository.userRepository, repository.rentHistoryRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}