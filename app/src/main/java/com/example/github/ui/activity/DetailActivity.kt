package com.example.github.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.github.R
import com.example.github.ui.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_COMPANY = "extra_company"
        const val EXTRA_LOCATION = "extra_location"
        const val EXTRA_REPOSITORY = "extra_repository"
        const val EXTRA_FOLLOWER = "extra_follower"
        const val EXTRA_FOLLOWING = "extra_following"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initializeData()
        initializeAdapter()

    }

    private fun initializeData() {

        val bundle = intent.extras

        if(bundle?.get(EXTRA_COMPANY).toString() == "null") {
            lineCompany.visibility = View.GONE
            ivCompany.visibility = View.GONE
            tvCompany.visibility = View.GONE
        }else{
            tvCompany.text = bundle?.get(EXTRA_COMPANY).toString()
        }

        if(bundle?.get(EXTRA_LOCATION).toString() == "null") {
            lineLocation.visibility = View.GONE
            ivLocation.visibility = View.GONE
            tvCompany.visibility = View.GONE
        }else{
            tvLocation.text = bundle?.get(EXTRA_LOCATION).toString()
        }

        tvName.text = bundle?.get(EXTRA_NAME).toString()
        tvRepository.text = bundle?.get(EXTRA_REPOSITORY).toString()
        tvFollowers.text = bundle?.get(EXTRA_FOLLOWER).toString()
        tvFollowing.text = bundle?.get(EXTRA_FOLLOWING).toString()

        Glide.with(this)
            .load(bundle?.get(EXTRA_AVATAR).toString())
            .apply(RequestOptions().override(90, 90))
            .into(civAvatar)

        title = bundle?.get(EXTRA_LOGIN).toString()
    }

    private fun initializeAdapter() {
        val pagerAdapter = PagerAdapter(this, supportFragmentManager)
        vpPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(vpPager)

        supportActionBar?.elevation = 0f
    }

}