package com.example.github.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.github.R
import com.example.github.model.User
import com.example.github.ui.activity.DetailActivity
import kotlinx.android.synthetic.main.list_item.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItems: User) {
            with(itemView) {
                tvName.text = userItems.name
                tvUsername.text = userItems.login

                Glide.with(context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(60, 60))
                    .into(civAvatar)

                setOnClickListener {
                    val mIntent = Intent(context, DetailActivity::class.java)

                    mIntent.apply {
                        putExtra(DetailActivity.EXTRA_AVATAR, userItems.avatar)
                        putExtra(DetailActivity.EXTRA_LOGIN, userItems.login)
                        putExtra(DetailActivity.EXTRA_NAME, userItems.name)
                        putExtra(DetailActivity.EXTRA_COMPANY, userItems.company)
                        putExtra(DetailActivity.EXTRA_LOCATION, userItems.location)
                        putExtra(DetailActivity.EXTRA_REPOSITORY, userItems.repository)
                        putExtra(DetailActivity.EXTRA_FOLLOWER, userItems.followers)
                        putExtra(DetailActivity.EXTRA_FOLLOWING, userItems.following)
                    }

                    context.startActivity(mIntent)
                    Toast.makeText(context, userItems.login, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

}