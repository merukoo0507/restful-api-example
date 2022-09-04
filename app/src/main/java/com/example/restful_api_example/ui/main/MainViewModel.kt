package com.example.restful_api_example.ui.main

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restful_api_example.remote.DataRepository
import com.example.restful_api_example.remote.Result
import com.example.restful_api_example.remote.model.User
import com.example.restful_api_example.util.Constants.TOTAL_USER_LINIT
import com.example.restful_api_example.util.Constants.USER_NUM_PER_PAGE
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel: ViewModel() {
    private var _allUsers: MutableLiveData<List<User>> = MutableLiveData(listOf())
    val users: LiveData<List<User>> = _allUsers
    private var _showProgressBar = MutableLiveData(false)
    val showProcessBar = _showProgressBar
    private var _errorMsg = MutableLiveData("")
    val errorMsg = _errorMsg
    var searchKeyWords = MutableLiveData("")
    var since = 1
    var page = 1

    init {
        getAllUsers()
    }

    private fun getAllUsers() {
        Timber.d("getUsers size: ${_allUsers.value!!.size}, since: $since")
        viewModelScope.launch {
            showProcessBar.value = true
            DataRepository.getUsers(since).let {
                when(it) {
                    is Result.Success -> {
                        _allUsers.value = _allUsers.value?.plus(it.data)
                        since = it.data.last().id
                        showProcessBar.value = false
                    }
                    is Result.Error -> {
                        _errorMsg.value = it.toString()
                        showProcessBar.value = false
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }

    private fun getSearchUsers() {
        Timber.d("getSearchUsers: ${searchKeyWords.value.toString()}, page:$page")
        viewModelScope.launch {
            showProcessBar.value = true
            DataRepository.searchUsers(searchKeyWords.value.toString(), page).let {
                when(it) {
                    is Result.Success -> {
                        _allUsers.value = _allUsers.value?.plus(it.data.items)
                        page++
                        showProcessBar.value = false
                    }
                    is Result.Error -> {
                        _errorMsg.value = it.toString()
                        showProcessBar.value = false
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }

    fun getUserRepos(page: Int, perPage: Int) {
        viewModelScope.launch {
            DataRepository.getUserRepos(page).let {
                when(it) {
                    is Result.Success -> {
                        showProcessBar.value = false
                    }
                    is Result.Error -> {
                        _errorMsg.value = it.toString()
                        showProcessBar.value = false
                    }
                    is Result.Loading -> {}
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
                    getAllUsers()
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
            getAllUsers()
        } else {
            page = 1
            getSearchUsers()
        }
    }

    fun addUser(user: User) {
        _allUsers.value = listOf(user).plus(_allUsers.value!!)
    }

    fun deleteUser(id: Int) {
        _allUsers.value?.forEachIndexed { index, user ->
            if (user.id == id)
                _allUsers.value = _allUsers.value?.minus(_allUsers.value!![index])
        }
    }

    fun updateUser(_user: User) {
        var list = _allUsers.value?.toMutableList()
        list?.forEachIndexed { index, user ->
            if (user.id == _user.id) {
                list[index] = _user
            }
        }
        _allUsers.value = list?.toList()
    }

    fun changePos(fromPos: Int, toPos: Int) {
        var list = _allUsers.value?.toMutableList()
        var _user = list?.removeAt(fromPos)
        if (_user != null) {
            list?.add(toPos, _user)
            _allUsers.value = list?.toList()
        }
    }
}