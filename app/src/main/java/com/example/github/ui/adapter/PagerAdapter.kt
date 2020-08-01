package com.example.github.ui.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.github.R
import com.example.github.ui.fragment.FollowersFragment
import com.example.github.ui.fragment.FollowingFragment
import com.example.github.ui.fragment.RepositoryFragment

class PagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val ints = intArrayOf(
        R.string.tab_repository,
        R.string.tab_follower,
        R.string.tab_following)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 ->
                RepositoryFragment()
            1 ->
                FollowersFragment()
            else ->
                FollowingFragment()
        }

    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(ints[position])
    }

    override fun getCount(): Int {
        return 3
    }
}