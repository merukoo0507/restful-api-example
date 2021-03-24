package com.example.gitRestSample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitRestSample.remote.DataRepository
import kotlinx.coroutines.launch
import com.example.gitRestSample.remote.Result.*
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.util.Constants
import com.example.gitRestSample.util.Constants.TOTAL_USER_LINIT
import timber.log.Timber

class MainViewModel(private val repo: DataRepository) : ViewModel() {

    private var _users: MutableLiveData<ArrayList<User>> = MutableLiveData(arrayListOf())
    val users: LiveData<ArrayList<User>> = _users

    init {
        _users.value = arrayListOf()
    }

    fun updateUserList() {
        if (users.value?.size == 0) {
            getUsers(1)
            return
        }
        users.value?.size?.div(Constants.USER_LINIT)?.let { getUsers(it) }
    }

    fun getUsers(page: Int) {
        if (_users.value?.size!! >= TOTAL_USER_LINIT) return
        viewModelScope.launch {
            repo.getUsers(page).let {
                if (it is Success) {
                    Timber.d("users size: ${it.data.size}")
                    val list = arrayListOf<User>()
                    list.addAll(_users.value!!.toList())
                    list.addAll(it.data)
                    _users.value = list
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