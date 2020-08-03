package com.example.mvvm_sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_sample.remote.DataRepository
import kotlinx.coroutines.launch
import com.example.mvvm_sample.remote.Result.*
import com.example.mvvm_sample.remote.model.User
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
                    Timber.d("users size: ${it.data.size}")
                    _users.value = it.data
                } else {
                    Timber.d(it.toString())
                }
            }
        }
    }

    fun getUserRepos(page: Int, perPage: Int) {
        viewModelScope.launch {
            repo.getUserRepos(page).let {
                if (it is Success) {
//                    _users.value = it.data
                } else {
                    Timber.d(it.toString())
                }
            }
        }
    }
}