package com.example.gitRestSample.ui.main

import androidx.lifecycle.*
import com.example.gitRestSample.remote.DataRepository
import kotlinx.coroutines.launch
import com.example.gitRestSample.remote.Result.*
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.util.Constants
import com.example.gitRestSample.util.Constants.TOTAL_PAGE_NUM
import com.example.gitRestSample.util.Constants.USER_NUM_PER_PAGE
import timber.log.Timber
import java.util.concurrent.locks.Lock

class MainViewModel(private val repo: DataRepository) : ViewModel() {
    private var _allUsers: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val users: MutableLiveData<List<User>> = _allUsers
    private var _showProgressBar = MutableLiveData(false)
    val showProcessBar = _showProgressBar
    private var _errorMsg = MutableLiveData("")
    val errorMsg = _errorMsg
    var searchKeyWords = MutableLiveData("")
    var page = 1

    private fun getAllUsers() {
        Timber.d("getAllUsers: ${searchKeyWords.value.toString()}, page:$page")
        viewModelScope.launch {
            showProcessBar.value = true
            repo.searchUsers(searchKeyWords.value.toString(), page).let {
                showProcessBar.value = false
                if (it is Success) {
                    val list = arrayListOf<User>()
                    list.addAll(_allUsers.value!!.toList())
                    list.addAll(it.data)
                    _allUsers.value = list
                } else {
                    _errorMsg.value = it.toString()
                }
            }
        }
    }

    //取得更多資料
    fun loadMoreUserList() {
        synchronized(showProcessBar) {
            if (showProcessBar.value == true) return
            showProcessBar.value = true
            page++
            Timber.d("loadMoreUserList() page: $page")
            if (page < TOTAL_PAGE_NUM) {
                getAllUsers()
            }
        }
    }

    //改變搜尋關鍵字，根據搜尋欄搜尋資料
    fun searchUserList(s: String = "") {
        Timber.d("updateUserList: $s")
        page = 1
        searchKeyWords.value = s
        _allUsers.value = listOf()
        getAllUsers()
    }

//    fun initialPage() {
//        Timber.d("initialPage, users size: ${users.value?.size}")
//        if (users.value!!.isEmpty()) {
//            searchKeyWords.value?.let {
//                updateUserList(it)
//            }?: updateUserList("")
//        }
//    }
}