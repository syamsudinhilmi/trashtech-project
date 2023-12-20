package com.bangkit.trashtech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.trashtech.databinding.ActivityAboutBinding
import com.bumptech.glide.Glide

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-project/master/CH2-PS163%20Member/Hilmi.jpg")
                .into(profileImage1)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-project/master/CH2-PS163%20Member/Fazril.jpg")
                .into(profileImage2)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-project/master/CH2-PS163%20Member/Fakhir.jpg")
                .into(profileImage3)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-project/master/CH2-PS163%20Member/Nathan.jpg")
                .into(profileImage4)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-project/master/CH2-PS163%20Member/Cesa.jpg")
                .into(profileImage5)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-project/master/CH2-PS163%20Member/Nabila.JPG")
                .into(profileImage6)
        }
    }
}