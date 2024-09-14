package com.example.matheasyapp.view.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.AdapterItemImagePdf
import com.example.matheasyapp.databinding.ActivityViewPdfactivityBinding
import com.example.matheasyapp.view.home.pdf.ViewModelPDF
import com.lyrebirdstudio.croppylib.ui.CroppedBitmapData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class   ViewPDFActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPdfactivityBinding
    private var urlPdf: String? = null
    private var titlePdf: String? = null
    private lateinit var pdfFile: File
    private var totalPageCount: Int = 0
    private lateinit var adapterPDF: AdapterItemImagePdf
    private val imagesMap: MutableMap<Int, Bitmap?> =
        mutableMapOf() // Có thể chứa null nếu chưa tải trang
    private var pdfRenderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null

    lateinit var viewModel: ViewModelPDF

    private var currentPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPdfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        urlPdf = intent.getStringExtra("url_pdf")
        titlePdf = intent.getStringExtra("title_pdf")

        if (urlPdf != null) {
            binding.tvTitlePDF.text = titlePdf
            pdfFile = File(urlPdf)

            lifecycleScope.launch {
                showLoadingIndicator()
                initializePdf()
                initializePageList() // Khởi tạo danh sách trang
                loadPage(0) {} // Tải trang đầu tiên
            }
        }

        setupRecyclerView()
        onClickEvent()
    }

    private fun setupRecyclerView() {
        adapterPDF = AdapterItemImagePdf(this@ViewPDFActivity, imagesMap)
        binding.rcvPDF.layoutManager =
            LinearLayoutManager(this@ViewPDFActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvPDF.adapter = adapterPDF

        binding.rcvPDF.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visiblePosition = layoutManager.findFirstVisibleItemPosition()
                    if (imagesMap[visiblePosition] == null) {
                        lifecycleScope.launch {
                            showLoadingIndicator()
                            loadPage(visiblePosition) {
                                binding.rcvPDF.smoothScrollToPosition(visiblePosition)
                                updateCurrentPage(visiblePosition + 1, totalPageCount)
                            }
                        }
                    }
                }
            }
        })
    }

    private suspend fun initializePdf() = withContext(Dispatchers.IO) {
        try {
            fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor!!)
            totalPageCount = pdfRenderer!!.pageCount
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initializePageList() {
        imagesMap.clear()
        for (i in 0 until totalPageCount) {
            imagesMap[i] = null
        }
        adapterPDF.notifyDataSetChanged()
    }

    private suspend fun loadPage(pageIndex: Int, onComplete: () -> Unit) {
        withContext(Dispatchers.IO) {
            val bitmap = renderPageBitmap(pageIndex)
            withContext(Dispatchers.Main) {
                imagesMap[pageIndex] = bitmap
                adapterPDF.notifyItemChanged(pageIndex)
                updateCurrentPage(pageIndex + 1, totalPageCount)
                hideLoadingIndicator()
                onComplete()
            }
        }
    }

    private fun hideLoadingIndicator() {
        binding.spinKit.visibility = View.GONE
        disableViewBottomNav(true, 1f)
    }

    private fun showLoadingIndicator() {
        binding.spinKit.visibility = View.VISIBLE
        disableViewBottomNav(false, 0.5f)
    }

    private fun disableViewBottomNav(value: Boolean, alpha: Float) {
        binding.layoutPrevious.isEnabled = value
        binding.layoutPrevious.alpha = alpha
        binding.searchPage.isEnabled = value
        binding.searchPage.alpha = alpha
        binding.layoutNext.isEnabled = value
    }

    private suspend fun renderPageBitmap(pageIndex: Int): Bitmap = withContext(Dispatchers.IO) {
        val page = pdfRenderer!!.openPage(pageIndex)
        val width = page.width * 2
        val height = page.height * 2
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.WHITE)

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        return@withContext bitmap
    }

    private fun onClickEvent() {

        binding.icBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.layoutPrevious.setOnClickListener {
            val layoutManager = binding.rcvPDF.layoutManager as LinearLayoutManager
            var currentPosition = layoutManager.findFirstVisibleItemPosition()
            val previousPosition = currentPosition - 1

            currentPage = previousPosition

            if (previousPosition >= 0) {
                lifecycleScope.launch {
                    if (imagesMap[previousPosition] == null) {
                        showLoadingIndicator()
                        loadPage(previousPosition) {
                            hideLoadingIndicator()
                            binding.rcvPDF.smoothScrollToPosition(previousPosition)
                            updateCurrentPage(previousPosition + 1, totalPageCount)
                        }
                    } else {
                        hideLoadingIndicator()
                        binding.rcvPDF.smoothScrollToPosition(previousPosition)
                        updateCurrentPage(previousPosition + 1, totalPageCount)
                    }
                }
            }
        }

        binding.layoutNext.setOnClickListener {
            val layoutManager = binding.rcvPDF.layoutManager as LinearLayoutManager
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val nextPosition = currentPosition + 1

            currentPage = nextPosition

            if (nextPosition < totalPageCount) {
                lifecycleScope.launch {
                    if (imagesMap[nextPosition] == null) {
                        showLoadingIndicator()
                        loadPage(nextPosition) {
                            hideLoadingIndicator()
                            binding.rcvPDF.smoothScrollToPosition(nextPosition)
                            updateCurrentPage(nextPosition + 1, totalPageCount)
                        }
                    } else {
                        hideLoadingIndicator()
                        binding.rcvPDF.smoothScrollToPosition(nextPosition)
                        updateCurrentPage(nextPosition + 1, totalPageCount)
                    }
                }
            }

        }

        binding.searchPage.setOnClickListener {
            val dialog = Dialog(this@ViewPDFActivity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_search_page)

            val window: Window? = dialog.window
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.CENTER)

            val inputPage = dialog.findViewById<EditText>(R.id.edPage)
            val btnGo = dialog.findViewById<Button>(R.id.btnOk)

            btnGo.setOnClickListener {
                val pageNumber = inputPage.text.toString().toIntOrNull()
                if (pageNumber != null && pageNumber in 1..totalPageCount) {
                    val targetPosition = pageNumber - 1
                    currentPage = targetPosition
                    lifecycleScope.launch {
                        showLoadingIndicator()
                        if (imagesMap[targetPosition] == null) {
                            loadPage(targetPosition) {
                                binding.rcvPDF.scrollToPosition(targetPosition)
                                updateCurrentPage(targetPosition + 1, totalPageCount)
                                dialog.dismiss()
                            }
                        } else {
                            binding.rcvPDF.scrollToPosition(targetPosition)
                            updateCurrentPage(targetPosition + 1, totalPageCount)
                            dialog.dismiss()
                        }
                        hideLoadingIndicator()
                    }
                }
            }

            dialog.show()
        }

        binding.icCheckPdf.setOnClickListener {
                handleCrop()
        }
    }

    private fun updateCurrentPage(currentPage: Int, totalPage: Int) {
        val value = "$currentPage / $totalPage"
        binding.tvIndexPDF.text = value
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfRenderer?.close()
        fileDescriptor?.close()
    }


    private fun handleCrop() {
        val viewHolder =
            binding.rcvPDF.findViewHolderForAdapterPosition(currentPage) as? AdapterItemImagePdf.ImageViewHolder
        val cropView = viewHolder?.cropView
        cropView?.let {
            val croppedBitmap: CroppedBitmapData? = cropView.getCroppedData()
            croppedBitmap?.croppedBitmap?.let { bitmap ->
                val file = File(this.cacheDir, "image.png")
                bitmapToFile(bitmap, file)?.let { resultFile ->
                    val intent = Intent(this, ResponseActivity::class.java)
                    intent.putExtra("photo_file_path", resultFile.absolutePath)
                    startActivity(intent)
                }
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, file: File): File? {
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }


}