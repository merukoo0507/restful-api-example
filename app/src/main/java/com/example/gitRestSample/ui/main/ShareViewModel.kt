package com.example.gitRestSample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitRestSample.remote.DataRepository
import com.example.gitRestSample.remote.Result
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.remote.model.UserDetail
import kotlinx.coroutines.launch
import timber.log.Timber

class ShareViewModel: ViewModel() {
    var user = MutableLiveData<User>()
}