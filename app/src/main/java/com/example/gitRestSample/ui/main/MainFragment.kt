package com.example.gitRestSample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitRestSample.R
import com.example.gitRestSample.ViewModelFactory
import com.example.gitRestSample.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

class MainFragment: Fragment() {
    lateinit var binding: FragmentMainBinding
    lateinit var viewmodel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel = ViewModelProvider(this,
            ViewModelFactory.instance
        ).get(MainViewModel::class.java)

        viewmodel.updateUserList()
        recycle_view.layoutManager = LinearLayoutManager(requireContext())
        recycle_view.adapter = UserAdapter(requireContext(), viewmodel.users, {
            Timber.d("position: $it")
            if (it == (viewmodel.users.value?.size?.minus(1))) {
                viewmodel.updateUserList()
            }
        }) {
            val bundle = Bundle()
            bundle.putString("name", it)
            findNavController().navigate(R.id.action_mainFragment_to_profile_fragment, bundle)
        }

        viewmodel.users.observe(viewLifecycleOwner, Observer { users ->
            Timber.d("users size: ${users.size}")
            (recycle_view.adapter as UserAdapter).notifyDataSetChanged()
        })
    }
}