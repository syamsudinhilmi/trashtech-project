package com.bangkit.trashtech.ui.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.trashtech.R
import com.bangkit.trashtech.databinding.ActivityDetailNewsBinding
import com.bangkit.trashtech.ui.viewmodel.NewsViewModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding
    private lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[NewsViewModel::class.java]

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_backspace)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@DetailNewsActivity, R.color.primary)))
        }

        viewModel.setDetailNews(USERNAME, "4cc5bb3ac6fe4898ac97bddf67335362")
        showLoading(true)
        viewModel.detailNews.observe(this){article ->
            showLoading(false)
            binding.apply {
                tvTitle.text = article[0].title
                tvSource.text = article[0].source.name
                Glide.with(this@DetailNewsActivity)
                    .load(article[0].urlToImage)
                    .into(ivNewsImg)
                tvPublishedAt.text = formatDate(article[0].publishedAt)
                tvDescription.text = article[0].content
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

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun formatDate(dateString: String?): String {
        // Check if dateString is not null and not empty
        if (!dateString.isNullOrBlank()) {
            // Parse the input date string
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)

            // Format the date to the desired format
            val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            return outputFormat.format(date)
        }
        return ""
    }

    companion object{
        var USERNAME = ""
    }
}