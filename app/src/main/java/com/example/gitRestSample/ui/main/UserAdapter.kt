package com.example.gitRestSample.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitRestSample.R
import com.example.gitRestSample.databinding.ItemUserBinding
import com.example.gitRestSample.remote.model.User
import com.example.gitRestSample.util.Constants
import kotlinx.android.synthetic.main.item_user.view.*
import timber.log.Timber

class UserAdapter(
    private val context: Context,
    private val loadMoreUserList: () -> Unit,
    private val onUserClick: (user: User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserHolder>() {
    private var users: ArrayList<User> = arrayListOf()

    class UserHolder(val itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(
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
            itemview.setOnClickListener {
                onUserClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    // Called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(users[position], context, onUserClick)
        Timber.d("onBindViewHolder $position")
        if (position > itemCount - Constants.PRE_LOAD) {
            loadMoreUserList()
        }
    }

    fun updateData(it: List<User>) {
        users.clear()
        users.addAll(it)
        notifyDataSetChanged()
    }
}