package com.example.matheasyapp.view.home


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Picture
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.ActivityMainBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.view.history.historyscan.HistoryCameraActivity
import com.example.matheasyapp.view.history.HistoryActivity
import com.example.matheasyapp.view.history.HistoryEmptyActivity
import com.example.matheasyapp.view.home.pdf.PDFFragment
import com.example.matheasyapp.view.tool.ToolFragment


class MainActivity : AppCompatActivity() {

    private lateinit var database: HistoryDatabase

    private lateinit var binding: ActivityMainBinding

    private var currentFragmentTag: String? = null

    private lateinit var viewModelToolbar: ViewModelToolbar


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        database = HistoryDatabase.getDatabase(this)

        // set opacity & underline for toolbar
        viewModelToolbar = ViewModelProvider(this).get(ViewModelToolbar::class.java)


        binding.iconHistory.setOnClickListener {
            when (currentFragmentTag) {

                "calculator" -> {
                    val intent: Intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }

                "scanner" -> {
                    val intent: Intent = Intent(this, HistoryCameraActivity::class.java)

                    startActivity(intent)
                }

                "pdf" -> {
                    val intent: Intent = Intent(this, HistoryEmptyActivity::class.java)

                    startActivity(intent)
                }

                "tool" -> {
                    val intent: Intent = Intent(this, HistoryEmptyActivity::class.java)

                    startActivity(intent)
                }
            }

        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ScannerFragment()).commitNow()
        currentFragmentTag = "scanner"
        binding.bottomNavigation.menu.findItem(R.id.scanner_bottomnav).isChecked = true
        hideToolbar()


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.caculator_bottomnav -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CaculatorFragment()).commitNow()
                    currentFragmentTag = "calculator"
                    Handler(Looper.getMainLooper()).postDelayed({
                        showToolbar()

                    }, 50)

                    true
                }

                R.id.image_bottomnav -> {

                    binding.layoutToolbar.post {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, PictureFragment()).commitNow()
                        currentFragmentTag = "scanner"
                    }

                    hideToolbar()

                    true
                }

                R.id.scanner_bottomnav -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ScannerFragment())
                        .commitNow()
                    currentFragmentTag = "scanner"

                    hideToolbar()

                    true
                }

                R.id.pdf_bottomnav -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PDFFragment()).commitNow()
                    currentFragmentTag = "pdf"

                    Handler(Looper.getMainLooper()).postDelayed({
                        showToolbar()
                    }, 150)

                    true
                }

                R.id.tools_bottomnav -> {
                        binding.layoutToolbar.setBackgroundColor(Color.WHITE)

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ToolFragment()).commitNow()
                        currentFragmentTag = "tool"


                    Handler(Looper.getMainLooper()).postDelayed({
                        showToolbar()
                    }, 150)

                    true
                }

                else -> false
            }
        }

    }

    fun showToolbar() {
        binding.layoutToolbar.visibility = View.VISIBLE
//        binding.layoutToolbar.alpha = 1f
    }

    fun hideToolbar() {
        binding.layoutToolbar.visibility = View.GONE
//        binding.layoutToolbar.alpha = 0f
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this!!.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

//    fun navigateToPictureFragment() {
//
//        val bundle = Bundle()
//        bundle.putString("keyString", "unit")
//
//        val calculator = CaculatorFragment()
//        calculator.arguments = bundle
//
//        val pictureFragment = PictureFragment()
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, pictureFragment)
//            .addToBackStack(null)
//            .commit()
//         hideToolbar()
//
//
//    }

}