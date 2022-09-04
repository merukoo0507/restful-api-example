package com.example.restful_api_example.ui.profile

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.restful_api_example.R
import com.example.restful_api_example.ui.main.ShareViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val shareViewModel: ShareViewModel by activityViewModels()
    private val viewmodel: ProfileViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        shareViewModel.user.value?.let {
            viewmodel.getUser(it.login)
        }
        viewmodel.user.observe(viewLifecycleOwner, Observer {
            Timber.d("user: $it")
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