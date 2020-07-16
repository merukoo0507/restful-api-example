package com.example.mvvm_sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_sample.remote.User
import kotlinx.coroutines.launch
import com.example.mvvm_sample.remote.Result.*
import timber.log.Timber

class MainViewModel(private val repo: DataRepository) : ViewModel() {

    private var _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun getUsers(page: Int) {
        viewModelScope.launch {
            repo.getUsers(page).let {
                if (it is Success) {
                    _users.value = it.data
                } else {
                    Timber.d(it.toString())
                }
            }
        }
    }

    fun getUser(name: String) {
        viewModelScope.launch {
            repo.getUser(name).let {
                if (it is Success) {
                    _user.value = it.data
                } else {
                    Timber.d(it.toString())
                }
            }
        }
    }
}