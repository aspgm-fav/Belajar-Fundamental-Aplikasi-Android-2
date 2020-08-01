package com.example.github.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.github.R
import com.example.github.model.User
import kotlinx.android.synthetic.main.list_item.view.*

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepoViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(repoItems: User) {
            with(itemView) {
                tvName.text = repoItems.name
                tvUsername?.visibility = View.GONE

                Glide.with(context)
                    .load(repoItems.avatar)
                    .apply(RequestOptions().override(60, 60))
                    .into(civAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RepoViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(mData[position])
    }

}