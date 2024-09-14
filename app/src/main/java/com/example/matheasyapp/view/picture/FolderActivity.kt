package com.example.matheasyapp.view.picture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityFolderBinding
import com.example.matheasyapp.view.picture.adapter.FolderAdapter

class FolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFolderBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val folderMap = intent.getSerializableExtra("folderMap") as Map<String, List<String>>

        val folderAdapter = FolderAdapter(folderMap, { folderName ->
            val resultIntent = Intent()
            resultIntent.putExtra("selectedFolder", folderName)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = folderAdapter


    }
}