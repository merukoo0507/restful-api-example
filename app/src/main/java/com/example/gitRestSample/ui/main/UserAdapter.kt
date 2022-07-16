package com.example.gitRestSample.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitRestSample.AppExecutors
import com.example.gitRestSample.R
import com.example.gitRestSample.databinding.ItemUserBinding
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.util.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_user.view.*
import timber.log.Timber

class UserAdapter(
    private val context: Context,
    private val loadMoreUserList: () -> Unit,
    private val onUserClick: (user: User) -> Unit
) : ListAdapter<User, UserAdapter.UserHolder>(
    AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            var gson = Gson()
            return gson.toJson(oldItem) == gson.toJson(newItem)
        }
    }).setBackgroundThreadExecutor(AppExecutors.diskIO()).build()
) {
    class UserHolder(val itemview: ItemUserBinding) : RecyclerView.ViewHolder(itemview.root)

    fun bind(
        itemview: ItemUserBinding,
        user: User,
        context: Context,
        onUserClick: (user: User) -> Unit
    ) {
        itemview.login.text = user.login
        Glide.with(context)
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
        bind(holder.itemview, getItem(position), context, onUserClick)
        if (position > itemCount - Constants.PRE_LOAD) {
            loadMoreUserList()
        }
    }
}