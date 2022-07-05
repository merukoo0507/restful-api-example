package com.example.gitRestSample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitRestSample.remote.DataRepository
import com.example.gitRestSample.remote.Result.Success
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.util.Constants.TOTAL_USER_LINIT
import com.example.gitRestSample.util.Constants.USER_NUM_PER_PAGE
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel: ViewModel() {
    private var _allUsers: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val users: MutableLiveData<List<User>> = _allUsers
    private var _showProgressBar = MutableLiveData(false)
    val showProcessBar = _showProgressBar
    private var _errorMsg = MutableLiveData("")
    val errorMsg = _errorMsg
    var searchKeyWords = MutableLiveData("")
    var since = 1
    var page = 1

    init {
        _allUsers.value = arrayListOf()
    }

    private fun getUsers() {
        Timber.d("getUsers size: ${_allUsers.value!!.size}, since: $since")
        viewModelScope.launch {
            showProcessBar.value = true
            DataRepository.instance.getUsers(since).let {
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

    private fun getSearchUsers() {
        Timber.d("getSearchUsers: ${searchKeyWords.value.toString()}, page:$page")
        viewModelScope.launch {
            showProcessBar.value = true
            DataRepository.instance.searchUsers(searchKeyWords.value.toString(), page).let {
                showProcessBar.value = false
                if (it is Success) {
                    val list = arrayListOf<User>()
                    list.addAll(_allUsers.value!!.toList())
                    list.addAll(it.data.items)
                    _allUsers.value = list
                    page++
                } else {
                    _errorMsg.value = it.toString()
                }
            }
        }
    }

    fun getUserRepos(page: Int, perPage: Int) {
        viewModelScope.launch {
            DataRepository.instance.getUserRepos(page).let {
                if (it is Success) {
//                    _allUsers.value = it.data
                } else {
                    _errorMsg.value = it.toString()
                }
            }
        }
    }

    //取得更多資料
    fun loadMoreUserList() {
        Timber.d("loadMoreUserList")
        synchronized(showProcessBar) {
            if (showProcessBar.value == true) return
            if ((users.value!!.size + USER_NUM_PER_PAGE) < TOTAL_USER_LINIT) {
                if (searchKeyWords.value!!.isNullOrEmpty()) {
                    getUsers()
                } else {
                    getSearchUsers()
                }
            }
        }
    }

    //改變搜尋關鍵字，根據搜尋欄搜尋資料
    fun searchUserList(s: String = "") {
        Timber.d("updateUserList: $s")
        searchKeyWords.value = s
        _allUsers.value = listOf()
        if (s.isNullOrEmpty()) {
            since = 1
            getUsers()
        } else {
            page = 1
            getSearchUsers()
        }
    }
}