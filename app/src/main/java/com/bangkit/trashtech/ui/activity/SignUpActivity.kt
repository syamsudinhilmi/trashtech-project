package com.bangkit.trashtech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bangkit.trashtech.R
import com.bangkit.trashtech.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth
        binding.btnSignUp.setOnClickListener{
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val pass = binding.edRegisterPass.text.toString()
            val confPass = binding.edConfRegisterPass.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confPass.isNotEmpty()){
                if (pass == confPass){
                    showLoading(true)
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            showLoading(false)
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(this, "Berhasil menambahkan", Toast.LENGTH_SHORT).show()
//                              val user = auth.currentUser
                                resetInput()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Gagal menambahkan", Toast.LENGTH_SHORT).show()

                            }
                        }
                } else{
                    Toast.makeText(this, "input password dan konfirmasi tidak sama", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Kolom input wajib diisi", Toast.LENGTH_SHORT).show()
            }
            
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun resetInput(){
        binding.edRegisterEmail.setText("")
        binding.edRegisterName.setText("")
        binding.edRegisterPass.setText("")
        binding.edConfRegisterPass.setText("")
    }

}