package com.example.mvvm_sample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_sample.R
import com.example.mvvm_sample.ViewModelFactory
import com.example.mvvm_sample.databinding.FragmentMainBinding
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

        for (i in 1..5) {
            viewmodel.getUsers(i)
        }
        viewmodel.users.observe(viewLifecycleOwner, Observer { it ->
            Timber.d("users size: ${it.size}")
            recycle_view.layoutManager = LinearLayoutManager(requireContext())
            recycle_view.adapter = UserAdapter(requireContext(), it) {
                val bundle = Bundle()
                bundle.putString("name", it)
                findNavController().navigate(R.id.action_mainFragment_to_profile_fragment, bundle)
            }
        })
    }
}