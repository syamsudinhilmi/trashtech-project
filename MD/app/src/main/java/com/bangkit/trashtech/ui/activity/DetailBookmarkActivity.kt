package com.bangkit.trashtech.ui.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.trashtech.R
import com.bangkit.trashtech.databinding.ActivityDetailBookmarkBinding
import com.bangkit.trashtech.ui.viewmodel.BookmarkViewModel
import com.bumptech.glide.Glide

class DetailBookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBookmarkBinding
    private lateinit var bookmarkVM: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_backspace)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@DetailBookmarkActivity, R.color.primary)))
        }

        showLoading(true)
        bookmarkVM.getNewsByTitle(NEWSTITLE).observe(this){ data ->
            showLoading(false)
            data.forEach{article ->
                binding.apply {
                    tvTitle.text = article.title
                    tvSource.text = article.source
                    Glide.with(this@DetailBookmarkActivity)
                        .load(article.urlImage)
                        .into(ivNewsImg)
                    tvPublishedAt.text = article.publishedAt
                    tvDescription.text = article.content
//                    tvUrlSource.text = "berita lengkap : " + article.urlNews
                    tvUrlSource.text = getString(R.string.full_news, article.urlNews)
                    btnMark.setBackgroundColor(ContextCompat.getColor(this@DetailBookmarkActivity, R.color.destroy))
                    btnMark.text = getString(R.string.delete_text)

                    btnMark.setOnClickListener{
                        bookmarkVM.deleteNewsByTitle(NEWSTITLE)
                        finish()
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnMark.visibility = if (state) View.GONE else View.VISIBLE
    }

    companion object{
        var NEWSTITLE = ""
    }
}