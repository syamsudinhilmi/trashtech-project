package com.bangkit.trashtech.ui.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.trashtech.R
import com.bangkit.trashtech.adapter.BookmarkAdapter
import com.bangkit.trashtech.databinding.ActivityBookmarkBinding
import com.bangkit.trashtech.ui.viewmodel.BookmarkViewModel

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var bookmarkVM: BookmarkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_backspace)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@BookmarkActivity, R.color.primary)))
        }

        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]

        // recyclerview
        val adapter = BookmarkAdapter()
        val recyclerView = binding.rvNews
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        bookmarkVM.allData.observe(this){data ->
            if (data.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
                binding.tvEmpty2.visibility = View.VISIBLE
            }else{
                adapter.setData(data)   
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}