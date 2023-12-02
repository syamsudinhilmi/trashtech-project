package com.playdeadrespawn.trashtech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.playdeadrespawn.trashtech.R
import com.playdeadrespawn.trashtech.databinding.ActivityProfileBinding
import com.playdeadrespawn.trashtech.ui.BottomNavigationUtils

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bottomNavView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.profile
        bottomNavView.setOnItemSelectedListener { item ->
            BottomNavigationUtils.handleBottomNavigation(this, item.itemId)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                BottomNavigationUtils.startMainActivity(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}