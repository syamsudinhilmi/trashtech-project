package com.bangkit.trashtech.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.trashtech.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var documentReference: DocumentReference
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth
        fStore = Firebase.firestore

        binding.signin.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val pass = binding.edRegisterPass.text.toString()
            val confPass = binding.edConfRegisterPass.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confPass.isNotEmpty()) {
                if (pass == confPass) {
                    showLoading(true)
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this) { task ->
                            showLoading(false)
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                userID = auth.currentUser?.uid.toString()
                                documentReference = fStore.collection("users").document(userID)
                                val user = hashMapOf(
                                    "username" to name,
                                    "email" to email,
                                    "password" to pass
                                )
                                documentReference.set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Berhasil menambahkan", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, SigninActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent) 
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                                        resetInput()
                                    }
                                
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Gagal menambahkan", Toast.LENGTH_SHORT).show()
                                Toast.makeText(
                                    this,
                                    task.exception?.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "input password dan konfirmasi tidak sama",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
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