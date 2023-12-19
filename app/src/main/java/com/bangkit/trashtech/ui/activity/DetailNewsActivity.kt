package com.bangkit.trashtech.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.trashtech.R
import com.bangkit.trashtech.data.database.News
import com.bangkit.trashtech.data.response.Article
import com.bangkit.trashtech.databinding.ActivityDetailNewsBinding
import com.bangkit.trashtech.ui.viewmodel.BookmarkViewModel
import com.bangkit.trashtech.ui.viewmodel.NewsViewModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Suppress("NAME_SHADOWING")
class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var bookmarkVM: BookmarkViewModel
    private lateinit var link: String
    private lateinit var title: String
    private lateinit var  image: String
    private lateinit var author: String
    private lateinit var urlNews: String
    private lateinit var source: String
    private lateinit var publishedAt: String
    private lateinit var content: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        bookmarkVM = ViewModelProvider(this)[BookmarkViewModel::class.java]

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_backspace)
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@DetailNewsActivity,
                        R.color.primary
                    )
                )
            )
        }

        viewModel.setDetailNews(NEWSTITLE, "4cc5bb3ac6fe4898ac97bddf67335362")

        showLoading(true)
        viewModel.detailNews.observe(this) { articles ->
            showLoading(false)
            if (articles.isNotEmpty()) {
                val article = articles[0]
                setData(article)
                title = article.title
                image = article.urlToImage ?: ""
                author = article.author ?: ""
                urlNews = article.url
                source = article.source?.name ?: ""
                publishedAt = article.publishedAt ?: ""
                content = article.content ?: ""
                link = article.url
            }
        }


        binding.tvUrlSource.setOnClickListener {
            if (::link.isInitialized && link.isNotBlank()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            } else {
                Toast.makeText(this, "Link is not available", Toast.LENGTH_SHORT).show()
            }
        }

        bookmarkVM.getNewsByTitle(NEWSTITLE).observe(this) { existingData ->
            val data = existingData.isNotEmpty()
            savedButton(data)

            binding.btnMark.setOnClickListener {
                if (data) {
                    bookmarkVM.deleteNewsByTitle(title)
                } else {
                    // Check if the title is not blank before saving the news
                    if (title.isNotBlank()) {
                        val news = News(
                            0,
                            title,
                            image,
                            urlNews,
                            source,
                            author,
                            publishedAt,
                            content,
                            true
                        )
                        bookmarkVM.addNews(news)
                        Toast.makeText(this, "Berita disimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Judul tidak tersedia, berita tidak dapat disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun savedButton(data: Boolean) {
        if(data){
            binding.btnMark.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_primary))
            binding.btnMark.text = getString(R.string.saving_text)
        }else{
            binding.btnMark.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            binding.btnMark.text = getString(R.string.save_text)
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
        binding.btnMark.visibility = if (state) View.GONE else View.VISIBLE
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
            return outputFormat.format(date as Date)
        }
        return ""
    }

    private fun setData(article: Article) {
        binding.apply {
            tvTitle.text = article.title
            tvSource.text = article.source?.name ?: ""
            Glide.with(this@DetailNewsActivity)
                .load(article.urlToImage)
                .into(ivNewsImg)
            tvPublishedAt.text = formatDate(article.publishedAt)
            tvDescription.text = article.content
            tvUrlSource.text = getString(R.string.full_news, article.url)
        }
    }

    companion object{
        var NEWSTITLE = ""
    }
}