package com.example.restful_api_example.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restful_api_example.remote.DataRepository
import com.example.restful_api_example.remote.Result.Success
import com.example.restful_api_example.remote.model.User
import com.example.restful_api_example.util.Constants.TOTAL_USER_LINIT
import com.example.restful_api_example.util.Constants.USER_NUM_PER_PAGE
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
    var updateStatus = MutableLiveData(false)

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
                    updateStatus.value = true

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
                    updateStatus.value = true

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
                    // 設定Repo資料
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

    fun addUser(user: User) {
        var list: ArrayList<User> = arrayListOf()
        list.add(user)
        list.addAll(users.value!!)
        _allUsers.value = list
    }

    fun deleteUser(id: Int) {
        var list: List<User> = _allUsers.value!!.filter { it.id != id }
        _allUsers.value = list
    }

    fun updateUser(user: User) {
        var list: ArrayList<User> = arrayListOf()
        list.addAll(users.value!!)
        list.forEachIndexed { index, it ->
            if (it.id == user.id) {
                list[index] = user
                return@forEachIndexed
            }
        }
        _allUsers.value = list
    }

    fun changePos(fromPos: Int, toPos: Int) {
        _allUsers.value?.get(fromPos)?.let {
            var list: ArrayList<User> = arrayListOf()
            var oldList = _allUsers.value!!.filterIndexed { index, user -> index != fromPos }
            list.addAll(oldList)
            list.add(toPos, it)
            _allUsers.value = list
        }
    }
}