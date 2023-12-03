package com.playdeadrespawn.trashtech.ui.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.playdeadrespawn.trashtech.R
import com.playdeadrespawn.trashtech.databinding.ActivityProfileBinding
import com.playdeadrespawn.trashtech.ui.BottomNavigationUtils

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
//        setSupportActionBar(binding.materialToolBarProfile)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        binding.actionLogout.setOnClickListener{
            startActivity(Intent(this, SigninActivity::class.java))
        }

        binding.actionBack.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    override fun onStart() {
        val bottomNavView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.profile
        bottomNavView.itemActiveIndicatorColor = getColorStateList(R.color.dark_primary)
        bottomNavView.setOnItemSelectedListener  { item ->
            BottomNavigationUtils.handleBottomNavigation(this, item.itemId)
            true
        }
        Log.d("start", "onstart")
        super.onStart()
    }
}