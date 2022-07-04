package com.example.gitRestSample.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitRestSample.R
import com.example.gitRestSample.ViewModelFactory
import com.example.gitRestSample.databinding.FragmentMainBinding
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.ui.profile.ProfileFragment
import com.example.gitRestSample.util.Constants.PRE_LOAD
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

class MainFragment: Fragment(R.layout.fragment_main) {
    // Use the 'by viewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val viewmodel: MainViewModel by viewModels {
        ViewModelFactory.instance
    }
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.searchUserList()

        userAdapter = UserAdapter(requireContext(),
            { viewmodel.loadMoreUserList() }) {
            val bundle = Bundle()
            bundle.putString("name", it)
            findNavController().navigate(R.id.action_mainFragment_to_profile_fragment, bundle)
//            parentFragmentManager.commit {
//                add(R.id.nav_host_fragment_container, ProfileFragment::class.java, bundle, null)
//                addToBackStack(null)
//            }
        }
    }

    override fun onResume() {
        super.onResume()

        recycle_view.layoutManager = LinearLayoutManager(requireContext())
        recycle_view.adapter = userAdapter

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

        // Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()
        viewmodel.updateState.observe(viewLifecycleOwner, Observer {
            if (it) {
                val users = viewmodel.users.value!!
                Timber.d("users size: ${users.size}")
                userAdapter.updateData(users)
                viewmodel.updateState.value = false
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

        add_item.setOnClickListener {
            var user = User(1, "https://avatars.githubusercontent.com/u/2?v=4", "defunkt", false)
            var users: ArrayList<User> = arrayListOf()
            users.add(user)
            users.addAll(viewmodel.users.value!!)
            viewmodel.users.value = users

            userAdapter.addData(0, user)
            recycle_view.scrollToPosition(0)
        }
    }
}