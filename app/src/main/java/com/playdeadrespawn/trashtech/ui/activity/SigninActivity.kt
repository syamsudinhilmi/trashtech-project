package com.playdeadrespawn.trashtech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.playdeadrespawn.trashtech.R
import com.playdeadrespawn.trashtech.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignin.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.signin.setOnClickListener(){
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}