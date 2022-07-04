package com.example.gitRestSample.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitRestSample.R
import com.example.gitRestSample.databinding.ItemUserBinding
import com.example.gitRestSample.remote.model.User

class UserAdapter(
    private val context: Context,
    private val users: MutableLiveData<List<User>>,
    private val posNotify: (pos: Int) -> Unit,
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
        users.value?.let { return it.size } ?:let { return 0}
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        users.value?.let {user ->
            holder.binding.login.text = user[position].login
            Glide.with(context)
                .load(user[position].avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.binding.avatar)
            holder.binding.itemLayout.setOnClickListener {
                onUserClick(user[position].login)
            }
            posNotify(position)
        }
    }
}