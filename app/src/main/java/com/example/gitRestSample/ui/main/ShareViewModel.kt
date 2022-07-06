package com.example.gitRestSample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitRestSample.remote.model.User

class ShareViewModel: ViewModel() {
    var user = MutableLiveData<User>()
}