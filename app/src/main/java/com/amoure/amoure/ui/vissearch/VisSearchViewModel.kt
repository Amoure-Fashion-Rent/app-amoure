package com.amoure.amoure.ui.vissearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VisSearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is vis search Fragment"
    }
    val text: LiveData<String> = _text
}