package com.bangkit.trashtech.ui.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.trashtech.R
import com.bangkit.trashtech.adapter.HistoryAdapter
import com.bangkit.trashtech.data.database.History
import com.bangkit.trashtech.databinding.ActivityHistoryBinding
import com.bangkit.trashtech.ui.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_backspace)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@HistoryActivity, R.color.primary)))
        }

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onResume() {
        historyViewModel.allHistory.observe(this){data ->
            setData(data)
        }
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.delete -> {
                deleteHistory()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData(data: List<History>){
        val adapter = HistoryAdapter()
        val recyclerView = binding.rvHistory
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (data.isEmpty()){
            binding.tvEmpty.visibility = View.VISIBLE
            binding.tvEmpty2.visibility = View.VISIBLE
        }else{
            adapter.setData(data)
        }
    }

    private fun deleteHistory() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getString(R.string.alert_title_delete))
        alertDialogBuilder.setMessage(getString(R.string.alert_desc_delete))
        alertDialogBuilder.setPositiveButton(getString(R.string.yes_text)) { _, _ ->
            historyViewModel.deleteAll()
            Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
        }

        alertDialogBuilder.setNegativeButton(getString(R.string.no_text)) { _, _ ->
            finish()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)
        return true
    }
}