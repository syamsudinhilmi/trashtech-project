package com.playdeadrespawn.trashtech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.playdeadrespawn.trashtech.R
import com.playdeadrespawn.trashtech.databinding.ActivityMainBinding
import com.playdeadrespawn.trashtech.ui.BottomNavigationUtils
import com.playdeadrespawn.trashtech.ui.BottomNavigationUtils.handleBottomNavigation

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnGalery.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
        }

        val bottomNavView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.home
        bottomNavView.setOnItemSelectedListener  { item ->
            BottomNavigationUtils.handleBottomNavigation(this, item.itemId)
            true
        }

    }
}