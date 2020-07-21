package com.example.mvvm_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    lateinit var viewmodel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewmodel = ViewModelProvider(this, ViewModelFactory.instance).get(MainViewModel::class.java)

        viewmodel.getUserRepos(2, 20)
        viewmodel.users.observe(this, Observer { it ->
            it.forEach {
                Timber.d(it.login)
            }
        })
    }
}