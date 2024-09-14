package com.example.matheasyapp.view.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.matheasyapp.databinding.ActivityHistoryEmptyBinding

class HistoryEmptyActivity : AppCompatActivity() {


    private lateinit var binding: ActivityHistoryEmptyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryEmptyBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressed()
            finish()

        }
        binding.btnAddHistory.setOnClickListener {
            onBackPressed()
            finish()
        }

    }
}