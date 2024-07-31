package com.example.matheasyapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.view.calculate.HistoryActivity
import com.example.matheasyapp.view.calculate.CaculatorFragment
import com.example.matheasyapp.view.calculate.UnitConverFragment
import com.example.matheasyapp.view.tool.ToolFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    lateinit var  iconHistory : ImageView

    private lateinit var database: HistoryDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        iconHistory = findViewById(R.id.iconHistory)

        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE)

        database = HistoryDatabase.getDatabase(this)

        iconHistory.setOnClickListener {

            val intent: Intent = Intent(this, HistoryActivity::class.java)

            startActivity(intent)
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CaculatorFragment()).commit()
        bottomNav.menu.findItem(R.id.scanner_bottomnav).isChecked = true

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.caculator_bottomnav -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CaculatorFragment()).commit()
                    true
                }
                R.id.image_bottomnav -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ImageFragment()).commit()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, UnitConverFragment()).commit()

                    true
                }
                R.id.scanner_bottomnav -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ScannerFragment()).commit()
                    true
                }
                R.id.pdf_bottomnav -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PDFFragment()).commit()
                    true
                }
                R.id.tools_bottomnav -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ToolFragment()).commit()
                    true
                }
                else -> false
            }
        }

    }


}