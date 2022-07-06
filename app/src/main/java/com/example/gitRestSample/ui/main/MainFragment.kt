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
import com.example.gitRestSample.remote.model.User
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

        refreshPage()
        recycleview()
        observers()
        events()
    }

    fun refreshPage() {
        viewmodel.updateStatus.value = true
    }

    private fun recycleview() {
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
    }

    fun observers() {
        // Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()
        viewmodel.updateStatus.observe(viewLifecycleOwner, Observer {
            if (!it) return@Observer
            viewmodel.users.value?.let {
                Timber.d("users size: ${it.size}")
                userAdapter.updateDatas(it)
            }
            viewmodel.updateStatus.value = false
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

    fun events() {
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

        btn_show_action.setOnClickListener {
            if (ll_actions.visibility == View.GONE) ll_actions.visibility = View.VISIBLE
            else if (ll_actions.visibility == View.VISIBLE) ll_actions.visibility = View.GONE
        }

        add_item.setOnClickListener {
            var user = User(id, edit_avatar_url.text.toString(), edit_login.text.toString(), false)
            viewmodel.addUser(user)
            userAdapter.addData(0, user)
            recycle_view.scrollToPosition(0)
        }

        delete_item.setOnClickListener {
            if (edit_id.text.toString().isNullOrEmpty()) return@setOnClickListener
            var id = Integer.parseInt(edit_id.text.toString())
            viewmodel.deleteUser(id)
            var idx = userAdapter.deleteData(id)
            if (idx >= 0)   recycle_view.scrollToPosition(idx)
        }

        update_item.setOnClickListener {
            if (edit_id.text.toString().isNullOrEmpty()) return@setOnClickListener
            var user = User(Integer.parseInt(edit_id.text.toString()), edit_avatar_url.text.toString(), edit_login.text.toString(), false)
            viewmodel.updateUser(user)
            var idx = userAdapter.updateData(user)
            if (idx >= 0) recycle_view.scrollToPosition(idx)
        }

        change_position.setOnClickListener {
            if (edit_from_pos.text.toString().isNullOrEmpty()
                || edit_to_pos.text.toString().isNullOrEmpty()) return@setOnClickListener

            var fromPos = Integer.parseInt(edit_from_pos.text.toString())
            var toPos = Integer.parseInt(edit_to_pos.text.toString())
            userAdapter.moveData(fromPos, toPos)
            recycle_view.scrollToPosition(toPos)
        }
    }
}