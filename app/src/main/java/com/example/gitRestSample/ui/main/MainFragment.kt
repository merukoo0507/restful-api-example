package com.example.gitRestSample.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitRestSample.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

class MainFragment: Fragment(R.layout.fragment_main) {
    // Use the 'by viewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val viewmodel: MainViewModel by viewModels()

    private val shareViewModel: ShareViewModel by activityViewModels()

    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.loadMoreUserList()
    }

    override fun onStart() {
        super.onStart()

        userAdapter = UserAdapter(requireContext(),
            { viewmodel.loadMoreUserList() }) {
            shareViewModel.user.value = it
            findNavController().navigate(R.id.action_mainFragment_to_profile_fragment)
//            parentFragmentManager.commit {
//                add(R.id.nav_host_fragment_container, ProfileFragment::class.java, bundle, null)
//                addToBackStack(null)
//            }
        }
        recycle_view.layoutManager = LinearLayoutManager(requireContext())
        recycle_view.adapter = userAdapter

        // Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()
        viewmodel.users.observe(viewLifecycleOwner, Observer {
            Timber.d("users size: ${it.size}")
            userAdapter.updateData(it)
        })

        search_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.d("onTextChanged: $s")
                viewmodel.searchUserList("$s")
            }

            override fun afterTextChanged(s: Editable?) {
            }

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