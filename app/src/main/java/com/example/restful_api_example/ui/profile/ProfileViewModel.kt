package com.example.restful_api_example.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restful_api_example.remote.DataRepository
import com.example.restful_api_example.remote.Result
import com.example.restful_api_example.remote.model.UserDetail
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel: ViewModel() {
    private var _user = MutableLiveData<UserDetail>()
    val user: LiveData<UserDetail> = _user

    private var _showProgressBar = MutableLiveData(false)
    val showProcessBar = _showProgressBar
    private var _errorMsg = MutableLiveData("")
    val errorMsg = _errorMsg

    fun getUser(name: String) {
        viewModelScope.launch {
            showProcessBar.value = true
            DataRepository.getUser(name).let {
                showProcessBar.value = false
                if (it is Result.Success) {
                    Timber.d(it.data.login)
                    _user.value = it.data
                } else {
                    _errorMsg.value = it.toString()
                }
            }
        }
    }
}