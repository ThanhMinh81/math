package com.example.matheasyapp.view.home.pdf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.FragmentPdfBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.model.PdfFile
import com.google.android.material.tabs.TabLayout
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class PDFFragment : Fragment() {

    private lateinit var binding: FragmentPdfBinding

    lateinit var viewModel: ViewModelPDF

    private var itemPopChecked: String = "None"

    private lateinit var pdfPickerLauncher: ActivityResultLauncher<Intent>;

    private lateinit var database: HistoryDatabase


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPdfBinding.inflate(inflater, container, false)

        binding.viewpagerpdf.adapter = ViewPagerAdapter(this)

        database = HistoryDatabase.getDatabase(requireActivity())

        binding.viewpagerpdf.setCurrentItem(0)

        viewModel = ViewModelProvider(requireActivity()).get(ViewModelPDF::class.java)

        pdfPickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {

                    result.data?.data?.let { uri ->
                        val pdfFile: PdfFile? = getPdfFileFromUri(uri)
                        val time = getCurrentDateTime()
                        pdfFile!!.time = time
                        val id: Long = database.pdfStart().insert(pdfFile!!)
                        Log.d("52345234525", id.toString())
                        viewModel.setValuePDFScan(pdfFile)
                    }

                }
            }

        binding.layoutSearch.visibility = View.GONE

        viewModel.setShareMode("pdf")

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("PDF"))

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("START"))

//        binding.tabLayout.setText(R.style.CustomTabTextAppearance)

        binding.viewpagerpdf.setUserInputEnabled(false)

        eventClick()

        handleSearch()

        return binding.root
    }


    private fun getPdfFileFromUri(uri: Uri): PdfFile? {
        val contentResolver = requireContext().contentResolver
        var pdfFile: PdfFile? = null

        // Chỉ định các cột cần lấy
        val projection = arrayOf(
            OpenableColumns.DISPLAY_NAME,
            OpenableColumns.SIZE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED
        )

        // Truy vấn ContentResolver để lấy thông tin
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                // Lấy tên file và kích thước file
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                val lastModifiedIndex =
                    cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED)

                val fileName = cursor.getString(nameIndex) ?: "Unknown"
                val fileSizeBytes = cursor.getLong(sizeIndex)
                val fileSizeKB = fileSizeBytes / 1024.0
                val fileSizeFormatted = String.format("%.2f KB", fileSizeKB)

                // Lấy thời gian sửa đổi cuối cùng (nếu có)
                val fileTimeStamp =
                    if (lastModifiedIndex != -1) cursor.getLong(lastModifiedIndex) else System.currentTimeMillis()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(fileTimeStamp))

                // Lấy đường dẫn file (có thể trả về null nếu không xác định được)
                val filePath = getFilePathFromUri(uri)

                // Lấy thời gian hiện tại với giờ phút giây
                val currentTime = dateFormat.format(Date())

                // Tạo đối tượng PdfFile
                pdfFile = PdfFile(
                    id = 0,
                    path = filePath ?: "Unknown",
                    name = fileName,
                    size = fileSizeFormatted,
                    time = formattedDate, // Dùng thời gian sửa đổi
                    piority = false, // Bạn có thể chỉnh sửa tùy theo yêu cầu
                    timeEdit = currentTime // Dùng thời gian hiện tại làm timeEdit ban đầu
                )
            }
        }

        return pdfFile
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleSearch() {
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("504395702345725234", s.toString())
                viewModel.setSearchKeyWord(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.edSearch.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard(v as EditText)
            }
            false
        }

    }


    private fun eventClick() {

        val inputMethodManager = requireActivity()!!.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity()!!.currentFocus!!.windowToken, 0
            )
        }

        binding.icPopup.setOnClickListener {

            showCustomPopupMenu(it)

        }

        binding.icSearch.setOnClickListener {
            binding.layoutSearch.visibility = View.VISIBLE
            binding.layoutToolbar.visibility = View.GONE
            binding.viewpagerpdf.setCurrentItem(2)
        }

        binding.icCloseSearch.setOnClickListener {

            binding.edSearch.setText("")

        }

        binding.icBack.setOnClickListener {
            binding.layoutSearch.visibility = View.GONE
            binding.layoutToolbar.visibility = View.VISIBLE
            binding.viewpagerpdf.setCurrentItem(0)
            hideKeyboard(binding.edSearch)

        }

        binding.btnScanPDF.setOnClickListener {
            openPdfFilePicker()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab được chọn
                tab?.let {
                    val tabText = it.text
                    when (tabText) {
                        "PDF" -> {
                            viewModel.setShareMode("pdf")
                        }

                        "START" -> {
                            viewModel.setShareMode("start")
                        }
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab không còn được chọn
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab được chọn lại
            }
        })
    }

    private fun openPdfFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf" // Chỉ cho phép chọn các file PDF
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pdfPickerLauncher.launch(intent)
    }


    fun hideKeyboard(editText: EditText) {
        val imm =
            editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun showCustomPopupMenu(anchorView: View) {
        // Inflate the layout for the PopupWindow
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.popup_window_layout, null)
        val menuContainer = popupView.findViewById<LinearLayout>(R.id.menu_container)

        // Define menu items and their icons
        val menuItems = listOf(
            MenuItemData("Last modified", R.drawable.ic_list_up),
            MenuItemData("Name", R.drawable.sort_alpha),
            MenuItemData("File size", R.drawable.sort_size),
            MenuItemData("None", R.drawable.ic_none)
        )

        // Create the PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        // Populate menu items
        for (item in menuItems) {
            val menuItemView = inflater.inflate(R.layout.item_popup_layout, menuContainer, false)
            val titleTextView = menuItemView.findViewById<TextView>(R.id.menu_title)
            val iconImageView = menuItemView.findViewById<ImageView>(R.id.menu_icon)

            titleTextView.text = item.title
            iconImageView.setImageResource(item.iconResId)

            // Set custom text color for the item
            if (item.title == itemPopChecked) {
                titleTextView.setTextColor(Color.parseColor("#5352ed"))
            }

            // Set custom drawable color for the item
            if (item.title == itemPopChecked) {
                iconImageView.setColorFilter(Color.parseColor("#5352ed"), PorterDuff.Mode.SRC_IN)
            }

            menuItemView.setOnClickListener {

                itemPopChecked = item.title.toString()
                viewModel.setValueModeSort(item.title.toString().trim())

//                Toast.makeText(requireContext(), "You Clicked ${item.title}", Toast.LENGTH_SHORT)
//                    .show()
                // Dismiss popup window if needed
                popupWindow.dismiss()

            }

            menuContainer.addView(menuItemView)
        }

        // Calculate width percentage (e.g., 80% of screen width)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val popupWidth = (screenWidth * 0.6).toInt() // 80% of screen width

        // Set PopupWindow width and background
        popupWindow.width = popupWidth
        popupWindow.elevation = 50f

        popupWindow.showAsDropDown(anchorView)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.RED))
    }


    private fun getFilePathFromUri(uri: Uri): String? {
        val fileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r", null)
        return fileDescriptor?.fileDescriptor?.let {
            // Tạo tạm một file với tên và vị trí trên bộ nhớ nội bộ của ứng dụng
            val inputStream = FileInputStream(it)
            val tempFile =
                File(requireContext().cacheDir, getFileNameFromUri(uri) ?: "temp_pdf.pdf")
            val outputStream = FileOutputStream(tempFile)

            // Copy nội dung từ input stream sang file tạm
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Trả về đường dẫn của file tạm
            tempFile.absolutePath
        }
    }


    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }


    // Data class for menu items
    data class MenuItemData(val title: String, val iconResId: Int)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        // Lấy ngày và giờ hiện tại
        val now = LocalDateTime.now()

        // Định dạng ngày giờ theo kiểu "yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Chuyển đổi LocalDateTime thành chuỗi định dạng
        return now.format(formatter)
    }
}


//https://stackoverflow.com/questions/62782648/android-11-scoped-storage-permissions/67140033#67140033