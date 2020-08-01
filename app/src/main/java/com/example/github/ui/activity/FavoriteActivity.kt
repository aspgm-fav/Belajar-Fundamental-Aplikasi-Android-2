package com.example.github.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.github.R

class FavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        title = resources.getString(R.string.favorites)
    }
}