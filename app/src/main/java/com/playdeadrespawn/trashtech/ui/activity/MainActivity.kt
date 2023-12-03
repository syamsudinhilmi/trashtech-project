package com.playdeadrespawn.trashtech.ui.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.playdeadrespawn.trashtech.R
import com.playdeadrespawn.trashtech.databinding.ActivityMainBinding
import com.playdeadrespawn.trashtech.ui.BottomNavigationUtils
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary)))

        binding.btnCamera.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnGalery.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.btnKlasifikasi.setOnClickListener{
            startActivity(Intent(this, ResultActivity::class.java))
        }
    }

    override fun onStart() {
        val bottomNavView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.home
        bottomNavView.itemActiveIndicatorColor = getColorStateList(R.color.dark_primary)
        bottomNavView.setOnItemSelectedListener  { item ->
            BottomNavigationUtils.handleBottomNavigation(this, item.itemId)
            true
        }
        Log.d("start", "onstart")
        super.onStart()
    }
}