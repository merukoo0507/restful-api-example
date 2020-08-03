package com.example.mvvm_sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_sample.ui.main.MainViewModel
import com.example.mvvm_sample.remote.DataRepository
import com.example.mvvm_sample.ui.profile.ProfileViewModel

class ViewModelFactory(private var repo: DataRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with (modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                    repo
                )
                isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                    repo
                )
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T

    companion object {
        var instance = ViewModelFactory(DataRepository())
    }
}