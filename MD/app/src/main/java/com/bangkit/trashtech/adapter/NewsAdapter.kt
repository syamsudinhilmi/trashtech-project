package com.bangkit.trashtech.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.trashtech.data.response.Article
import com.bangkit.trashtech.databinding.ItemNewsBinding
import com.bangkit.trashtech.ui.activity.DetailNewsActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val list = ArrayList<Article>()
    @SuppressLint("NotifyDataSetChanged")
    fun setList(articles: ArrayList<Article>) {
        list.clear()
        list.addAll(articles)
        notifyDataSetChanged()
    }
    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(ivArticleImage)
                tvTitle.text = article.title
                tvSource.text = article.source.name
                tvPublishedAt.text = formatDate(article.publishedAt)
                tvDescription.text = article.description
            }
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            val title = list[position].title
//            Toast.makeText(holder.itemView.context, "Title: $title", Toast.LENGTH_SHORT).show()

            val detail = Intent(holder.itemView.context, DetailNewsActivity::class.java)
            DetailNewsActivity.NEWSTITLE = title
            holder.itemView.context.startActivity(detail)
        }
    }
}