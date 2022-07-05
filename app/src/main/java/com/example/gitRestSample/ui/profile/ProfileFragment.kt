package com.example.gitRestSample.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.gitRestSample.R
import com.example.gitRestSample.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewmodel: ProfileViewModel by viewModels {
        ViewModelFactory.instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = this.arguments
        bundle?.let {
            val name = it.get("name").toString()
            Timber.d("name: $name")
            viewmodel.getUser(name)
        }
    }

    override fun onStart() {
        super.onStart()

        viewmodel.user.observe(viewLifecycleOwner, Observer {
            Timber.d("user: ${it.login}")
            Glide
                .with(requireContext())
                .load(it.avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(avatar)
            name.text = it.name
            login.text = it.login
            location.text = it.location
            blog.text = it.blog
        })
        viewmodel.errorMsg.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Timber.d("errorMsg $it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        viewmodel.showProcessBar.observe(viewLifecycleOwner, Observer {
            if (it) requireActivity().progressBar.visibility = View.VISIBLE
            else requireActivity().progressBar.visibility = View.INVISIBLE
        })
    }
}