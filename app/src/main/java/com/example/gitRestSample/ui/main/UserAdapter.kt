package com.example.gitRestSample.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitRestSample.R
import com.example.gitRestSample.databinding.ItemUserBinding
import com.example.gitRestSample.remote.model.User

class UserAdapter(
    private val context: Context,
    private val users: List<User>,
    private val onUserClick: (name: String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserHolder>() {
    class UserHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        var inflate = LayoutInflater.from(parent.context)
        var binding = ItemUserBinding.inflate(inflate, parent, false)
        return UserHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.binding.login.text = users[position].login
        Glide.with(context)
            .load(users[position].avatarUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.binding.avatar)
        holder.binding.itemLayout.setOnClickListener {
            onUserClick(users[position].login)
        }
    }
}