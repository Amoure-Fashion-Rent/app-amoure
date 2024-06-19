package com.amoure.amoure.ui.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.request.PostProductRequest
import com.amoure.amoure.data.response.CategoryItem
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.Profile
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile
    private val images = mutableListOf<String>()

    private val _categoryResults = MutableLiveData<List<CategoryItem?>>()
    val categoryResults: LiveData<List<CategoryItem?>> = _categoryResults

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String

    init {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    getCategory()
                }
            }
        }
    }

    fun postProductWithImages(imagesToUpload: List<MultipartBody.Part>, productRequest: PostProductRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                for (image in imagesToUpload) {
                    val imageUrl = postImage(image)
                    imageUrl?.let {
                        images.add(it)
                    }
                }
                productRequest.images = images
                postProduct(productRequest)
            } catch (e: Exception) {
                _isLoading.value = false
                _isError.value = true
            }
        }
    }

    private suspend fun postImage(multipartBody: MultipartBody.Part): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService(accessToken).postProductImage(multipartBody).execute()
                if (response.isSuccessful) {
                    response.body()?.data?.imageUrl
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun postProduct(req: PostProductRequest) {
        withContext(Dispatchers.IO) {
            try {
                val response = ApiConfig.getApiService(accessToken).postProduct(req).execute()
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    _isError.postValue(false)
                    images.clear()
                } else {
                    _isLoading.postValue(false)
                    _isError.postValue(true)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _isError.postValue(true)
            }
        }
    }
    private fun getCategory() {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getAllCategory()
        client.enqueue(object : Callback<InitialResponse<List<CategoryItem>>> {
            override fun onResponse(
                call: Call<InitialResponse<List<CategoryItem>>>,
                response: Response<InitialResponse<List<CategoryItem>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _categoryResults.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<List<CategoryItem>>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }
}