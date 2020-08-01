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
import com.example.github.ui.adapter.RepositoryAdapter
import com.example.github.viewmodel.RepositoryViewModel
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.fragment_repository.*
import kotlinx.android.synthetic.main.fragment_repository.pbLoading
import kotlinx.android.synthetic.main.fragment_repository.tvMessageErrorLoading
import kotlinx.android.synthetic.main.fragment_repository.tvMessageLoading

class RepositoryFragment : Fragment() {

    private var mAdapter = RepositoryAdapter()
    private lateinit var repositoryViewModel: RepositoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_repository, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        initializeAdapter()
        initializeRecyclerView()
        initializeViewModel()
    }

    private fun initializeAdapter() {
        mAdapter = RepositoryAdapter()
        mAdapter.notifyDataSetChanged()
    }

    private fun initializeRecyclerView() {
        rvRepository.layoutManager = LinearLayoutManager(context)
        rvRepository.adapter = mAdapter
    }

    private fun initializeViewModel() {
        val login = activity?.intent?.getStringExtra(DetailActivity.EXTRA_LOGIN)
        repositoryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RepositoryViewModel::class.java)
        repositoryViewModel.getRepos().observe(viewLifecycleOwner, Observer { repoItems ->
            if (repoItems != null) {
                mAdapter.setData(repoItems)
                showLoading(false)
                if (mAdapter.itemCount == 0) {
                    showErrorMessages(resources.getString(R.string.no_repository_message))
                } else {
                    rvRepository?.visibility = View.VISIBLE
                }
            }
        })

        val output =
            login?.let { repositoryViewModel.setRepos(it, mAdapter, activity!!.applicationContext) }
        if (output != null) showErrorMessages(output)
    }


    @SuppressLint("SetTextI18n")
    private fun showErrorMessages(message: String) {
        pbLoading?.visibility = View.GONE
        tvMessageLoading?.visibility = View.GONE
        tvMessageErrorLoading.visibility = View.VISIBLE
        tvMessageErrorLoading?.text = message
        rvRepository?.visibility = View.GONE
    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> {
                pbLoading?.visibility = View.VISIBLE
                tvMessageLoading?.visibility = View.VISIBLE
                tvMessageErrorLoading.visibility = View.GONE
                rvRepository?.visibility = View.GONE
            }
            false -> {
                pbLoading?.visibility = View.GONE
                tvMessageLoading?.visibility = View.GONE
            }
        }
    }
}