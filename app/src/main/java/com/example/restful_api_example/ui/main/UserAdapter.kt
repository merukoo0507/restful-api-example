package com.example.restful_api_example.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restful_api_example.R
import com.example.restful_api_example.databinding.ItemUserBinding
import com.example.restful_api_example.remote.model.User
import com.example.restful_api_example.util.Constants
import com.google.gson.Gson
import timber.log.Timber

class UserAdapter(
    private val onUserClick: (user: User) -> Unit
) : ListAdapter<User, UserAdapter.UserHolder>(ItemDiffCallback) {
    class UserHolder(val itemview: ItemUserBinding) : RecyclerView.ViewHolder(itemview.root)

    fun bind(
        itemview: ItemUserBinding,
        user: User,
        onUserClick: (user: User) -> Unit
    ) {
        itemview.login.text = user.login
        Glide.with(itemview.root.context)
            .load(user.avatarUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(itemview.avatar)
        itemview.root.setOnClickListener {
            onUserClick(user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        var binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context))
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserHolder(binding)
    }

    // Called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        Timber.d("onBindViewHolder $position")
        bind(holder.itemview, getItem(position), onUserClick)
    }
}

object ItemDiffCallback: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        var gson = Gson()
        return gson.toJson(oldItem) == gson.toJson(newItem)
    }
}