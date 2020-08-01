package com.example.github.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.R
import com.example.github.model.User
import com.example.github.ui.adapter.RepositoryAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class RepositoryViewModel : ViewModel() {
    val listRepos = MutableLiveData<ArrayList<User>>()

    fun setRepos(username: String, adapter: RepositoryAdapter, context: Context): String? {
        val repoList = ArrayList<User>()
        val token = context.resources.getString(R.string.token)
        var value: String? = null
        val url = "https://api.github.com/users/${username}/repos"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray) {
                val result = String(responseBody)
                val resultArray = JSONArray(result)

                for (i in 0 until resultArray.length()) {
                    val repoItems = User()
                    val repos = resultArray.getJSONObject(i)

                    repoItems.name = repos.getString("name")

                    val owner = repos.getJSONObject("owner")
                    repoItems.avatar = owner.getString("avatar_url")

                    repoList.add(repoItems)
                }

                listRepos.postValue(repoList)
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray?, error: Throwable?) {
                value = when (statusCode) {
                    401 -> "401: Bad Request"
                    403 -> "403: Access Forbidden"
                    404 -> "404: Not Found"
                    500 -> "500: Internal Server Error"
                    else -> "$statusCode: ${error?.message.toString()}"
                }
            }
        })
        return value
    }

    fun getRepos(): LiveData<ArrayList<User>> {
        return listRepos
    }
}