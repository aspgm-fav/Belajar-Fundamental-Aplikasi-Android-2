package com.example.github.ui.activity

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.R
import com.example.github.ui.adapter.UserAdapter
import com.example.github.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.pbLoading

class MainActivity : AppCompatActivity() {

    private var mAdapter = UserAdapter()
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeToolbar()
        initializeAdapter()
        initializeRecyclerView()
        initializeViewModel()
    }

    private fun initializeToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
    }

    private fun initializeAdapter() {
        mAdapter = UserAdapter()
        mAdapter.notifyDataSetChanged()
    }

    private fun initializeRecyclerView() {
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = mAdapter
        rvMain.visibility = View.GONE
    }

    private fun initializeViewModel() {
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                mAdapter.setData(userItems)
                showLoading(false)

                if (mAdapter.itemCount == 0) {
                    Toast.makeText(this, R.string.not_found_message, Toast.LENGTH_SHORT).show()
                    showErrorMessages(resources.getString(R.string.not_found_message))
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as? SearchView

        searchView?.queryHint = resources.getString(R.string.search_hint)
        searchView?.maxWidth = Integer.MAX_VALUE
        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty())
                    return true
                    showLoading(true)
                    val output = mainViewModel.setUsers(query, mAdapter, applicationContext)

                if (output != null)
                    showErrorMessages(output)
                    searchView.clearFocus()
                    return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_language ->
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))

            R.id.action_favorite ->
                startActivity(Intent(this, FavoriteActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    fun showLoading(isLoadMore: Boolean) {
        if (isLoadMore) {
            pbLoading.visibility = View.VISIBLE
            tvMessageLoading.visibility = View.VISIBLE
            tvMessageErrorLoading.visibility = View.GONE
            rvMain.visibility = View.GONE
        } else {
            pbLoading.visibility = View.GONE
            tvMessageLoading.visibility = View.GONE
            rvMain.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    fun showErrorMessages(text: String) {
        tvMessageErrorLoading.visibility = View.VISIBLE
        tvMessageErrorLoading.text =  text
    }
}