package com.bangkit.trashtech.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.trashtech.data.database.News
import com.bangkit.trashtech.databinding.ItemNewsBinding
import com.bangkit.trashtech.ui.activity.DetailBookmarkActivity
import com.bumptech.glide.Glide

class BookmarkAdapter: RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>(){
    private var newsList = emptyList<News>()

    inner class MyViewHolder(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(news: News) {
            binding.apply {
                Glide.with(itemView)
                    .load(news.urlImage)
                    .into(ivArticleImage)
                tvTitle.text = news.title
                tvSource.text = news.source
                tvPublishedAt.text = news.publishedAt
                tvDescription.text = news.content
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(newsList[position])

        holder.itemView.setOnClickListener {
            val title = newsList[position].title

            val detail = Intent(holder.itemView.context, DetailBookmarkActivity::class.java)
            DetailBookmarkActivity.NEWSTITLE = title
            holder.itemView.context.startActivity(detail)
        }
    }

    override fun getItemCount(): Int = newsList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(news: List<News>){
        this.newsList = news
        notifyDataSetChanged()
    }
}