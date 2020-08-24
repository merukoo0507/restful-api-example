package com.example.gitRestSample.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gitRestSample.R
import com.example.gitRestSample.ViewModelFactory
import com.example.gitRestSample.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var viewmodel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val name = savedInstanceState?.get("name").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel = ViewModelProvider(
            this,
            ViewModelFactory.instance
        ).get(ProfileViewModel::class.java)

        val bundle = this.arguments
        bundle?.let {
            val name = it.get("name").toString()
            Timber.d("name: $name")
            viewmodel.getUser(name)
        }

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
    }
}