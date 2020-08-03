package com.example.mvvm_sample.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_sample.remote.DataRepository
import kotlinx.coroutines.launch
import com.example.mvvm_sample.remote.Result.*
import com.example.mvvm_sample.remote.model.User
import com.example.mvvm_sample.remote.model.UserDetail
import timber.log.Timber

class ProfileViewModel(private val repo: DataRepository) : ViewModel() {
    private var _user = MutableLiveData<UserDetail>()
    val user: LiveData<UserDetail> = _user

    fun getUser(name: String) {
        viewModelScope.launch {
            repo.getUser(name).let {
                if (it is Success) {
                    Timber.d(it.data.login)
                    _user.value = it.data
                } else {
                    Timber.d(it.toString())
                }
            }
        }
    }
}