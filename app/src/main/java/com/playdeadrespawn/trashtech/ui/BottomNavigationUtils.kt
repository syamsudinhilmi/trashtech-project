package com.playdeadrespawn.trashtech.ui

import android.content.Context
import android.content.Intent
import com.playdeadrespawn.trashtech.R
import com.playdeadrespawn.trashtech.ui.activity.MainActivity
import com.playdeadrespawn.trashtech.ui.activity.MapsActivity
import com.playdeadrespawn.trashtech.ui.activity.ProfileActivity

object BottomNavigationUtils {
    fun handleBottomNavigation(context: Context, menuItemId: Int) {
        when (menuItemId) {
            R.id.home -> {
                startActivity(context, MainActivity::class.java)
            }
            R.id.maps -> {
                startActivity(context, MapsActivity::class.java)
            }
            R.id.profile -> {
                startActivity(context, ProfileActivity::class.java)
            }
        }
    }

    private fun <T> startActivity(context: Context, cls: Class<T>) {
        val intent = Intent(context, cls)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun startMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}