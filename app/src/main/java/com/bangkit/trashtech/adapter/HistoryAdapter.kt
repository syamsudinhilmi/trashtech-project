package com.bangkit.trashtech.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.trashtech.R
import com.bangkit.trashtech.data.database.History
import com.bangkit.trashtech.databinding.ItemHistoryBinding

import com.bumptech.glide.Glide

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyViewHolder>(){
    private var historyList = emptyList<History>()

    inner class MyViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(history: History) {
            binding.apply {
                Glide.with(itemView)
                    .load(history.urlImage)
                    .into(ivPredict)
                tvLabel.text = history.label
                tvRecommendation.text = history.recommendation
                tvDate.text = history.date
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(historyList[position])

        holder.itemView.setOnClickListener {
//            val id = historyList[position].id
//
//            // Menampilkan Toast dengan ID
//            Toast.makeText(holder.itemView.context, "ID: $id", Toast.LENGTH_SHORT).show()


            // Membuat dan menyiapkan dialog
            val builder = AlertDialog.Builder(holder.itemView.context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.custom_dialog_layout, null)
            builder.setView(dialogView)

            // Mengisi data ke dalam tampilan dialog
            val dialogImageView: ImageView = dialogView.findViewById(R.id.dialogImageView)
            val labelTextView: TextView = dialogView.findViewById(R.id.labelTextView)
            val recommendationTextView: TextView = dialogView.findViewById(R.id.recommendationTextView)
            val dateTextView: TextView = dialogView.findViewById(R.id.dateTextView)

            // Set gambar, label, rekomendasi, dan tanggal ke dalam tampilan dialog
            Glide.with(dialogImageView)
                .load(historyList[position].urlImage)
                .into(dialogImageView)
            labelTextView.text = historyList[position].label
            recommendationTextView.text = historyList[position].recommendation
            dateTextView.text = historyList[position].date

            // Menampilkan dialog
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int = historyList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(history: List<History>){
        this.historyList = history
        notifyDataSetChanged()
    }
}