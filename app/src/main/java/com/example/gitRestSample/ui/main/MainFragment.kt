package com.example.gitRestSample.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitRestSample.R
import com.example.gitRestSample.ViewModelFactory
import com.example.gitRestSample.databinding.FragmentMainBinding
import com.example.gitRestSample.util.Constants.PRE_LOAD
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

class MainFragment: Fragment(R.layout.fragment_main) {
    // Use the 'by viewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val viewmodel: MainViewModel by viewModels {
        ViewModelFactory.instance
    }

    override fun onStart() {
        super.onStart()

        viewmodel.loadMoreUserList()
        recycle_view.layoutManager = LinearLayoutManager(requireContext())
        recycle_view.adapter = UserAdapter(requireContext(), viewmodel.users, {
            Timber.d("onUserClick: $it")
            if (it == (viewmodel.users.value?.size?.minus(PRE_LOAD))) {
                viewmodel.loadMoreUserList()
            }
        }) {
            val bundle = Bundle()
            bundle.putString("name", it)
            findNavController().navigate(R.id.action_mainFragment_to_profile_fragment, bundle)
        }

        search_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.d("onTextChanged: $s")
                viewmodel.updateUserList("$s")
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        viewmodel.users.observe(viewLifecycleOwner, Observer {
            Timber.d("users size: ${it.size}")
            (recycle_view.adapter as UserAdapter).notifyDataSetChanged()
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