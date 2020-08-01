package com.example.github.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.ui.activity.DetailActivity
import com.example.github.ui.adapter.UserAdapter
import com.example.github.viewmodel.FollowersViewModel
import com.example.github.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_followers.*
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.fragment_following.pbLoading
import kotlinx.android.synthetic.main.fragment_following.tvMessageErrorLoading
import kotlinx.android.synthetic.main.fragment_following.tvMessageLoading

class FollowingFragment : Fragment() {

    private var mAdapter = UserAdapter()
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        initializeAdapter()
        initializeRecyclerView()
        initializeViewModel()
    }

    private fun initializeAdapter() {
        mAdapter = UserAdapter()
        mAdapter.notifyDataSetChanged()
    }

    private fun initializeRecyclerView() {
        rvFollowing.layoutManager = LinearLayoutManager(context)
        rvFollowing.adapter = mAdapter
    }


    private fun initializeViewModel() {
        val login = activity?.intent?.getStringExtra(DetailActivity.EXTRA_LOGIN)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        followingViewModel.getUsers().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                mAdapter.setData(userItems)
                showLoading(false)
                if (mAdapter.itemCount == 0) {
                    showErrorMessages(resources.getString(R.string.no_following_message))
                } else {
                    rvFollowing?.visibility = View.VISIBLE
                }
            }
        })

        val output =
            login?.let { followingViewModel.setUsers(it, mAdapter, activity!!.applicationContext) }
        if (output != null) showErrorMessages(output)
    }


    @SuppressLint("SetTextI18n")
    private fun showErrorMessages(message: String) {
        pbLoading?.visibility = View.GONE
        tvMessageLoading?.visibility = View.GONE
        tvMessageErrorLoading.visibility = View.VISIBLE
        tvMessageErrorLoading?.text = message
        rvFollowing?.visibility = View.GONE
    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> {
                pbLoading?.visibility = View.VISIBLE
                tvMessageLoading?.visibility = View.VISIBLE
                tvMessageErrorLoading.visibility = View.GONE
                rvFollowing?.visibility = View.GONE
            }
            false -> {
                pbLoading?.visibility = View.GONE
                tvMessageLoading?.visibility = View.GONE
            }
        }
    }
}