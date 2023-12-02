package com.playdeadrespawn.trashtech.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.playdeadrespawn.trashtech.R
import com.playdeadrespawn.trashtech.databinding.ActivityMainBinding
import com.playdeadrespawn.trashtech.getImageUri
import com.playdeadrespawn.trashtech.ui.BottomNavigationUtils
import com.playdeadrespawn.trashtech.ui.BottomNavigationUtils.handleBottomNavigation

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener{
            startCamera()
        }
        binding.btnGalery.setOnClickListener{
            startGallery()
        }
        binding.btnKlasifikasi.setOnClickListener{
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }

        val bottomNavView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.home
        bottomNavView.setOnItemSelectedListener  { item ->
            BottomNavigationUtils.handleBottomNavigation(this, item.itemId)
            true
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgPrev.setImageURI(it)
        }
    }
}