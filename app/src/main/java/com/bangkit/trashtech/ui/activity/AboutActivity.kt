package com.bangkit.trashtech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.trashtech.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.hide()
    }
}