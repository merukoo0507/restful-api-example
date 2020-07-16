package com.example.mvvm_sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private var repo: DataRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with (modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repo)
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T

    companion object {
        var instance = ViewModelFactory(DataRepository())
    }
}