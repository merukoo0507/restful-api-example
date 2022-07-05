package com.example.gitRestSample.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitRestSample.remote.model.User

class ShareViewModel : ViewModel() {
    var users = MutableLiveData<List<User>>(listOf())
}