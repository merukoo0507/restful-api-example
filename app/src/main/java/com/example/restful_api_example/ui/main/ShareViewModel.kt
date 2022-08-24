package com.example.restful_api_example.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restful_api_example.remote.model.User

class ShareViewModel: ViewModel() {
    var user = MutableLiveData<User>()
    var navigationDirection = MutableLiveData<String>("")
}