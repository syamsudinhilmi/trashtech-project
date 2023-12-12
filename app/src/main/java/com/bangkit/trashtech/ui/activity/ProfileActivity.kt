package com.bangkit.trashtech.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bangkit.trashtech.R
import com.bangkit.trashtech.databinding.ActivityProfileBinding
import com.bangkit.trashtech.ui.BottomNavigationUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            binding.tvRealEmail.text = user.email
        }

        binding.menuDropdown.setOnClickListener { showDropdownMenu(it)  }


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
        super.onStart()
    }

    private fun showDropdownMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.toolbar_menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_logout -> {
                    showAlertLogout()
                    true
                }
                R.id.menu_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showAlertLogout() {
        val builder = AlertDialog.Builder(this@ProfileActivity)
        builder.setTitle("Konfirmasi Keluar")
        builder.setMessage("Apakah anda yakin ingin mengeluarkan akun?")

        builder.setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
            logout()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun logout(){
        auth.signOut()
        val intent = Intent(this, SigninActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        Toast.makeText(this, "Berhasil keluar", Toast.LENGTH_SHORT).show()
    }
}