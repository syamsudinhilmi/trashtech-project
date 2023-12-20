package com.bangkit.trashtech.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.trashtech.R
import com.bangkit.trashtech.adapter.NewsAdapter
import com.bangkit.trashtech.databinding.ActivityMainBinding
import com.bangkit.trashtech.ui.BottomNavigationUtils
import com.bangkit.trashtech.ui.viewmodel.NewsViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var auth: FirebaseAuth

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.primary)))

        adapter = NewsAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[NewsViewModel::class.java]
        getUsername()

        binding.apply {
            rvNews.layoutManager = LinearLayoutManager(this@MainActivity)
            rvNews.setHasFixedSize(true)
            rvNews.adapter = adapter
            viewModel.setNews("sampah", "4cc5bb3ac6fe4898ac97bddf67335362")

            showLoading(true)
            viewModel.listNews.observe(this@MainActivity) { articles ->
                if (articles != null) {
                    showLoading(false)
                    adapter.setList(articles)
                }
            }

            btnIdentifikasi.setOnClickListener {
                val intent = Intent(this@MainActivity, IdentificationActivity::class.java)
                startActivity(intent)
            }

            btnSave.setOnClickListener{
                val intent = Intent(this@MainActivity, BookmarkActivity::class.java)
                startActivity(intent)
            }

            btnHistory.setOnClickListener{
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        val bottomNavView = binding.bottomNavView
        bottomNavView.selectedItemId = R.id.home
        bottomNavView.itemActiveIndicatorColor = getColorStateList(R.color.dark_primary)
        bottomNavView.setOnItemSelectedListener  { item ->
            BottomNavigationUtils.handleBottomNavigation(this, item.itemId)
            true
        }
        super.onStart()
    }

    private fun getUsername(){
        auth = Firebase.auth
        val user = auth.currentUser
        val db = Firebase.firestore

        if (user != null) {
            val docRef = db.collection("users").document(user.uid) //bug
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding.tvUser.text = document.get("username").toString()
                    }
                }

        }
    }
}