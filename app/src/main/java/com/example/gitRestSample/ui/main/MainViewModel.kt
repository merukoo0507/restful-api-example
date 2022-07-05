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
    val users: MutableLiveData<List<User>> = _allUsers
    private var _showProgressBar = MutableLiveData(false)
    val showProcessBar = _showProgressBar
    private var _errorMsg = MutableLiveData("")
    val errorMsg = _errorMsg
    var searchKeyWords = MutableLiveData("")
    var since = 1

    init {
        _allUsers.value = arrayListOf()
    }

    private fun getUsers() {
        viewModelScope.launch {
            showProcessBar.value = true
            repo.getUsers(since).let {
                showProcessBar.value = false
                if (it is Success) {
                    val list = arrayListOf<User>()
                    list.addAll(_allUsers.value!!.toList())
                    list.addAll(it.data)
                    _allUsers.value = list

                    since = it.data.last().id
                } else {
                    _errorMsg.value = it.toString()
                }
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
        if (since < TOTAL_USER_LINIT) {
            getUsers()
        }
    }

    fun updateUserList(s: String) {
        searchKeyWords.value = s
        if (s.isNullOrEmpty()) {
            since = 1
            getUsers()
            return
        }
        _allUsers.value = _allUsers.value?.filter {
            it.login?.contains(searchKeyWords.value.toString())
        }
    }
}