package com.example.github.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.R
import com.example.github.model.User
import com.example.github.ui.adapter.UserAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUsers(username: String, adapter: UserAdapter, context: Context): String? {
        val listItems = ArrayList<User>()
        val token = context.resources.getString(R.string.token)
        var value: String? = null
        val url = "https://api.github.com/users/${username}/following"
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                                   responseBody: ByteArray) {
                val result = String(responseBody)
                val resultArray = JSONArray(result)

                for (i in 0 until resultArray.length()) {
                    val userItems = User()
                    val users = resultArray.getJSONObject(i)
                    val urlDetail = "https://api.github.com/users/${users.getString("login")}"
                    client.get(urlDetail, object : AsyncHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                                               responseBody: ByteArray) {

                            val result2 = String(responseBody)
                            val userObject = JSONObject(result2)

                            userItems.name =
                                if (userObject.getString("name") == "null") ""
                                else userObject.getString("name")

                            userItems.company = userObject.getString("company")
                            userItems.location = userObject.getString("location")

                            userItems.login = users.getString("login")
                            userItems.avatar = users.getString("avatar_url")
                            userItems.repository = userObject.getInt("public_repos")
                            userItems.followers = userObject.getInt("followers")
                            userItems.following = userObject.getInt("following")

                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(statusCode: Int, headers: Array<out Header>?,
                                               responseBody: ByteArray?, error: Throwable?) {
                            Log.d("onFailure", error?.message.toString())
                            userItems.login = ""
                            userItems.avatar = ""
                            userItems.name = "?"
                            userItems.company = "?"
                            userItems.location = "?"
                            userItems.repository = 0
                            userItems.followers = 0
                            userItems.following = 0

                            adapter.notifyDataSetChanged()
                        }
                    })
                    listItems.add(userItems)
                }

                listUsers.postValue(listItems)
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

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}