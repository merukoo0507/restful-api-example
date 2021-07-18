package com.example.gitRestSample.ui.main

import androidx.lifecycle.*
import com.example.gitRestSample.remote.DataRepository
import kotlinx.coroutines.launch
import com.example.gitRestSample.remote.Result.*
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.util.Constants
import com.example.gitRestSample.util.Constants.TOTAL_USER_LINIT
import com.example.gitRestSample.util.Constants.USER_NUM_PER_PAGE
import timber.log.Timber

class MainViewModel(private val repo: DataRepository) : ViewModel() {

    private var _allUsers: MutableLiveData<List<User>> = MutableLiveData(listOf())
    private var _filterUsers: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val users: MutableLiveData<List<User>> = _filterUsers
    private var _showProgressBar = MutableLiveData(false)
    val showProcessBar = _showProgressBar
    private var _errorMsg = MutableLiveData("")
    val errorMsg = _errorMsg
    var searchKeyWords = MutableLiveData("")
    var since = 0

    init {
        _allUsers.value = arrayListOf()
    }

    private fun getAllUsers() {
        viewModelScope.launch {
            showProcessBar.value = true
            repo.getUsers(since).let {
                showProcessBar.value = false
                if (it is Success) {
                    val list = arrayListOf<User>()
                    list.addAll(_allUsers.value!!.toList())
                    list.addAll(it.data)
                    _allUsers.value = list
                    Timber.d("users size: ${list.size}")
                } else {
                    _errorMsg.value = it.toString()
                }
                updateUserList()
            }
        }
    }

    fun getUserRepos(page: Int, perPage: Int) {
        viewModelScope.launch {
            repo.getUserRepos(page).let {
                if (it is Success) {
//                    _allUsers.value = it.data
                } else {
                    _errorMsg.value = it.toString()
                }
            }
        }
    }

    fun loadMoreUserList() {
        Timber.d("loadMoreUserList() since: $since, size: ${_allUsers.value?.size}")
        if (since < TOTAL_USER_LINIT) {
            getAllUsers()
            since += USER_NUM_PER_PAGE
        }
    }

    fun updateUserList() {
        _filterUsers.value = _allUsers.value?.filter {
            it.login?.contains(searchKeyWords.value.toString())
        }
    }

    fun updateUserList(s: String) {
        searchKeyWords.value = s
        updateUserList()
    }
}