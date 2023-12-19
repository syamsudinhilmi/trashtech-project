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
import com.bangkit.trashtech.adapter.HistoryAdapter
import com.bangkit.trashtech.adapter.NewsAdapter
import com.bangkit.trashtech.data.database.History
import com.bangkit.trashtech.data.database.News
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
            title = "Berita Tersimpan"
            setHomeAsUpIndicator(R.drawable.ic_backspace)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@BookmarkActivity, R.color.primary)))
        }

        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]
    }

    override fun onResume() {
        bookmarkVM.allData.observe(this){data ->
            setData(data)
        }
        super.onResume()
    }

    private fun setData(data: List<News>){
        val adapter = BookmarkAdapter()
        val recyclerView = binding.rvBookmark
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (data.isEmpty()){
            binding.tvEmpty.visibility = View.VISIBLE
            binding.tvEmpty2.visibility = View.VISIBLE
        }else{
            adapter.setData(data)
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