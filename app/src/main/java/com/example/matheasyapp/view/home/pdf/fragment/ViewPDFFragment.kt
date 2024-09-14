package com.example.matheasyapp.view.home.pdf.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.PDFAdapter
import com.example.matheasyapp.databinding.FragmentViewPDFBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.model.PdfFile
import com.example.matheasyapp.view.home.ViewPDFActivity
import com.example.matheasyapp.view.home.pdf.ViewModelEditPDF
import com.example.matheasyapp.view.home.pdf.ViewModelPDF
import com.example.matheasyapp.view.home.pdf.bottomsheft.BottomsheftPDF
import com.example.matheasyapp.view.home.pdf.onClickItemPDF
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ViewPDFFragment : Fragment(), onClickItemPDF {

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    lateinit var binding: FragmentViewPDFBinding

    private lateinit var database: HistoryDatabase
    private var listEmpty: ArrayList<PdfFile> = ArrayList()

    private var adapter: PDFAdapter? = null

    var pdfFiles: ArrayList<PdfFile> = ArrayList()

    lateinit var viewModel: ViewModelPDF

    private val listStart: ArrayList<PdfFile> = ArrayList()

    private lateinit var viewModelEditPDF: ViewModelEditPDF

    //    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var requestAllFilesAccessLauncher: ActivityResultLauncher<Intent>
    private lateinit var openSettingsLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewPDFBinding.inflate(layoutInflater)

        database = HistoryDatabase.getDatabase(requireActivity())

        viewModel = ViewModelProvider(requireActivity()).get(ViewModelPDF::class.java)

        binding.rcvPDF.layoutManager = LinearLayoutManager(requireContext())
        adapter = PDFAdapter(requireContext(), pdfFiles, this, listEmpty)
        binding.rcvPDF.adapter = adapter

        viewModelEditPDF = ViewModelProvider(requireActivity()).get(ViewModelEditPDF::class.java)

//        // Initialize request all files access launcher
//        requestAllFilesAccessLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (isManageAllFilesAccessPermissionGranted()) {
//                loadPdfFiles()
//
//                binding.rcvPDF.visibility = View.VISIBLE
//                binding.layoutRequestImg.visibility = View.GONE
//
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14 (API 34) và cao hơn
            if (isManageAllFilesAccessPermissionGranted()) {
                loadPdfFiles()
                binding.rcvPDF.visibility = View.VISIBLE
                binding.layoutRequestImg.visibility = View.GONE
            } else {
                // Thực hiện hành động khi quyền "Manage All Files Access" chưa được cấp
                // Bạn có thể yêu cầu người dùng cấp quyền thông qua một Activity hoặc hướng dẫn
                promptForManageAllFilesAccessPermission()
            }
        } else {
            // Các phiên bản Android thấp hơn, yêu cầu quyền đọc bộ nhớ ngoài
            requestPermissions()
        }

        // Initialize open settings launcher
        openSettingsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            checkAndRequestPermissions()
        }



        initValueListStart()

        // Hide the keyboard if it's visible
        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken, 0
            )
        }

        // Button to access app settings
        binding.btnAccessPicture.setOnClickListener {
            if (!isManageAllFilesAccessPermissionGranted()) {
                requestAllFilesAccessPermission()
            } else {
                openAppSettings()
            }
        }

        if (isManageAllFilesAccessPermissionGranted()) {
            if (allPermissionsGranted()) {
                loadPdfFiles()
                getAllListStart()
            } else {
                requestPermissions()
                // phien ban android be hon 14
            }
        } else {
            requestAllFilesAccessPermission()
        }

        viewModel.getValue().observe(viewLifecycleOwner, Observer { value ->
            if (value == "pdf") {
                getAllListStart()
                adapter?.setChangeDisplay(true, listOf())
                adapter?.notifyDataSetChanged()
            } else if (value == "start") {
                getAllListStart()
                adapter?.setChangeDisplay(false, listOf())
                adapter?.notifyDataSetChanged()
            }
        })

        viewModel.getValueSort().observe(viewLifecycleOwner, Observer { value ->
            val sortedPdfFiles = when (value) {
                "Name" -> pdfFiles.sortedBy { it.name }
                "File size" -> pdfFiles.sortedBy { convertSizeToDouble(it.size) }
                "None" -> pdfFiles.sortedByDescending { it.piority }
                "Last modified" -> pdfFiles.sortedByDescending {
                    it.time
                }

                else -> pdfFiles
            }
            pdfFiles.sortedByDescending { it.time }
                .forEach { it -> Log.d("55252", it.time + " " + it.name) }
            adapter?.updateListPDF(sortedPdfFiles)
            adapter?.notifyDataSetChanged()
        })





        viewModel.getValuePDFScan().observe(viewLifecycleOwner, Observer { value ->

            if (value != null) {

                val intent = Intent(requireActivity(), ViewPDFActivity::class.java).apply {

                    putExtra("url_pdf", value.path)
                    putExtra("title_pdf", value.name)

                }
                startActivity(intent)
                val pdf: PdfFile? = null
                viewModel.setValuePDFScan(pdf)
                loadPdfFiles()
            }

        })

        handleEventBottomSheft()

        sortListPDF()

        return binding.root
    }

    private fun sortListPDF() {
        val value: String? = viewModel.getValueSort().value

        if (value != null) {
            val sortedPdfFiles = when (value) {
                "Name" -> pdfFiles.sortedBy { it.name }
                "File size" -> pdfFiles.sortedBy { convertSizeToDouble(it.size) }
                "None" -> pdfFiles.sortedByDescending { it.piority }
                "Last modified" -> pdfFiles.sortedByDescending {
                    it.time
                }

                else -> pdfFiles
            }
            pdfFiles.sortedByDescending { it.time }
                .forEach { it -> Log.d("55252", it.time + " " + it.name) }
            adapter?.updateListPDF(sortedPdfFiles)
            adapter?.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleEventBottomSheft() {

        // share file pdf
        viewModelEditPDF.getShareFile().observe(viewLifecycleOwner, Observer { value ->
//                dialogReadName(value!!)

        })

        viewModelEditPDF.getDeleteFile().observe(viewLifecycleOwner, Observer { value ->
            if (value != null) {
                showDialogDelete(value)
                viewModelEditPDF.setDeleteFile(null)


            }
        })


        // read name file pdf
        viewModelEditPDF.getReadNameFile().observe(viewLifecycleOwner, Observer { value ->
            // show dialog
            if (value != null) {
                dialogReadName(value!!)
                val pdf: PdfFile? = null
                viewModelEditPDF.setReadName(pdf)
            }

        })

        viewModelEditPDF.getStartPDF().observe(viewLifecycleOwner, Observer { value ->
            if (value != null) {
                var pdf: PdfFile = pdfFiles.first { it -> it.id == value!!.id }
                Log.d("453252", pdf.path)

                var index: Int = pdfFiles.indexOfFirst { it.id == value!!.id }
                Log.d("453252", index.toString())
                pdf.piority = value!!.piority
                database.pdfStart().update(pdf)
                viewModelEditPDF.setStartPDF(null)
                getAllListStart()

            }
        })


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialogDelete(pdf: PdfFile?) {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
        dialog.setContentView(R.layout.dialog_confirm_pdf)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancle: Button = dialog.findViewById(R.id.btnCancle)
        val btnOk: Button = dialog.findViewById(R.id.btnOk)

        btnCancle.setOnClickListener {
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            database.pdfStart().delete(pdf!!)
            loadPdfFiles()
            dialog.dismiss()
        }


// Set width và height của dialog là match_parent
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialog.window?.attributes
        layoutParams?.gravity = Gravity.CENTER

        dialog.window?.attributes = layoutParams
        dialog.show()

    }


    private fun initValueListStart() {
        val list: ArrayList<PdfFile> = ArrayList()
        list.addAll(database.pdfStart().getAllStart)
        viewModel.setListStart(listStart)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    private fun isManageAllFilesAccessPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true
        }
    }

    private fun requestAllFilesAccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.fromParts("package", requireActivity().packageName, null)
            }
            requestAllFilesAccessLauncher.launch(intent)
        } else {
            Log.e(
                "ViewPDFFragment",
                "All files access permission is not required for this Android version."
            )
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        openSettingsLauncher.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAndRequestPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                requireContext(), permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                binding.layoutRequestImg.visibility = View.GONE
                binding.rcvPDF.visibility = View.VISIBLE
                loadPdfFiles()
            }

            else -> {
                binding.layoutRequestImg.visibility = View.VISIBLE
                binding.rcvPDF.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadPdfFiles() {

        val listData: List<PdfFile> = database.pdfStart().getAllStart
        Log.d("4523523523525345", listData.size.toString())

        if (listData.isEmpty()) {
            // list pdf empty -> get on file pdf show screnn

            val pdf: PdfFile? = getSinglePdf()
            if (pdf != null) {
                pdfFiles.add(pdf)
                adapter!!.notifyDataSetChanged()
                viewModel.setShareListPDF(pdfFiles)
                val id: Long = database.pdfStart().insert(pdf)
            }
        } else {
            // list pdf not empty -> get all file pdf base path
            viewModel.setShareListPDF(pdfFiles)
            pdfFiles.clear()
            pdfFiles.addAll(listData)
            adapter!!.notifyDataSetChanged()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickItem(item: PdfFile) {
        val intent = Intent(requireActivity(), ViewPDFActivity::class.java).apply {
            putExtra("url_pdf", item.path)
            putExtra("title_pdf", item.name)
            val currentTime: String = getCurrentDateTime()
            item.time = currentTime.toString()
            database.pdfStart().update(item)
            loadPdfFiles()


        }
        startActivity(intent)
    }

    override fun onClickItemStart(item: PdfFile, start: Boolean) {
        if (start) {
            item.piority = true
            val newId = database.pdfStart().update(item)
            getAllListStart()


        } else {
            item.piority = false
            database.pdfStart().update(item)
            getAllListStart()
        }
    }

    override fun onClickItemEditPDF(item: PdfFile) {

        val bottomSheet = BottomsheftPDF().apply {
            arguments = Bundle().apply {
                putParcelable("pdf_edit_data", item)
            }
        }
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showBottomSheftPDF(item: PdfFile) {

        val dialog: Dialog = Dialog(requireContext())
        dialog?.apply {
            // Gọi requestWindowFeature trước khi thêm content
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
            setContentView(R.layout.bottomsheft_pdf_layout)  // Đặt nội dung sau khi gọi requestWindowFeature
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnEdit: LinearLayout = findViewById(R.id.layout_edit)

            val btnShare: LinearLayout = findViewById(R.id.layout_share)
            val btnDelete: LinearLayout = findViewById(R.id.layout_delete)

            btnEdit.setOnClickListener {
                dialog.dismiss()
                dialogReadName(item)
            }
            btnDelete.setOnClickListener {

            }
            btnShare.setOnClickListener {

            }

            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )

            val layoutParams = window?.attributes
            layoutParams?.gravity = Gravity.CENTER
            window?.attributes = layoutParams

            show()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dialogReadName(item: PdfFile) {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_name_pdf)

        val window: Window? = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.CENTER)

        val inputName = dialog.findViewById<EditText>(R.id.edName)
        val btnGo = dialog.findViewById<Button>(R.id.btnOk)
        val btnCancle = dialog.findViewById<Button>(R.id.btnCancle)

        inputName.setText(item.name)
        inputName.setSelection(inputName.text.length)

        btnGo.setOnClickListener {
            if (inputName.text.length > 0) {
                item.name = inputName.text.toString()
                // update database
                database.pdfStart().update(item)
                loadPdfFiles()
                dialog.dismiss()

            }
        }

        btnCancle.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun getAllListStart(): List<PdfFile> {

        val listData: List<PdfFile> = database.pdfStart().getAllWithPiority()
        Log.d("53252352", listData.size.toString())

        adapter?.setListPDFStart(listData)
        adapter!!.notifyDataSetChanged()
        if (listData.size > 0) {

            val startPaths = listData.map { it.id }.toSet()

            pdfFiles.forEach { pdfFile ->
                if (startPaths.contains(pdfFile.id)) {
                    pdfFile.piority = true
                } else {
                    pdfFile.piority = false
                }
            }

            val sortedPdfFiles = pdfFiles.sortedWith(compareByDescending { it.piority })

            sortedPdfFiles.forEach { it -> Log.d("93576932845423986", it.size.toString()) }

            adapter!!.updateListPDF(sortedPdfFiles)
            adapter!!.notifyDataSetChanged()
            adapter!!.sortPDF(viewModel.getListStart().value!!)
            viewModel.setListStart(listStart)


        }

        return listData

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSinglePdf(): PdfFile? {

        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(currentTime))  // Thời gian hiện tại

        val projection = arrayOf(
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED
        )

        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        val mimeType = "application/pdf"
        val selectionArgs = arrayOf(mimeType)
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val uri: Uri = MediaStore.Files.getContentUri("external")

        requireActivity().contentResolver.query(
            uri, projection, selection, selectionArgs, sortOrder
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val nameIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val dateIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)

                // Định dạng thời gian file được thêm vào thiết bị
                val fileTimeStamp = cursor.getLong(dateIndex)
                val fileDate = Date(fileTimeStamp * 1000)  // Convert timestamp to Date object
                val formattedFileDate = dateFormat.format(fileDate)  // Format the file added date

                val time = getCurrentDateTime()
                // Thông tin về file PDF
                val filePath = cursor.getString(dataIndex) ?: return null
                val fileName = cursor.getString(nameIndex) ?: "Unknown"
                val fileSizeBytes = cursor.getLong(sizeIndex)
                val fileSizeKB = fileSizeBytes / 1024.0

                return PdfFile(
                    0,
                    filePath,
                    fileName,
                    String.format("%.2f KB", fileSizeKB),
                    formattedFileDate,  // Thời gian file được lưu trên thiết bị
                    false,
                    time  // Bạn có thể sử dụng thời gian này nếu cần cho mục đích khác
                )
            } else {
                Log.e("PDFFragment", "Cursor is empty or could not be accessed.")
            }
        } ?: run {
            Log.e("PDFFragment", "Query failed. Cursor is null.")
        }

        return null
    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getSinglePdf(): PdfFile? {
//
//        val currentTime = System.currentTimeMillis()
//        val dateFormat = SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.getDefault())
//        val formattedDate = dateFormat.format(Date(currentTime))
//
//        val projection = arrayOf(
//            MediaStore.Files.FileColumns.DATA,
//            MediaStore.Files.FileColumns.DISPLAY_NAME,
//            MediaStore.Files.FileColumns.SIZE,
//            MediaStore.Files.FileColumns.DATE_ADDED
//        )
//
//        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
//        val mimeType = "application/pdf"
//        val selectionArgs = arrayOf(mimeType)
//        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
//
//        val uri: Uri = MediaStore.Files.getContentUri("external")
//
//        requireActivity().contentResolver.query(
//            uri, projection, selection, selectionArgs, sortOrder
//        )?.use { cursor ->
//            if (cursor.moveToFirst()) {
//                val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
//                val nameIndex =
//                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
//                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
//                val dateIndex =
//                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
//
//                val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
//
//                // Chỉ lấy tệp PDF đầu tiên
//                val filePath = cursor.getString(dataIndex) ?: return null
//                val fileName = cursor.getString(nameIndex) ?: "Unknown"
//                val fileSizeBytes = cursor.getLong(sizeIndex)
//                val fileSizeKB = fileSizeBytes / 1024.0
//                val fileTimeStamp = cursor.getLong(dateIndex)
//                val fileDate = Date(fileTimeStamp * 1000)
//
//                val formattedDate = dateFormat.format(fileDate)
//
//                val time = getCurrentDateTime()
//
//                return PdfFile(
//                    0,
//                    filePath,
//                    fileName,
//                    String.format("%.2f KB", fileSizeKB),
//                    time,
//                    false,
//                    formattedDate
//
//                )
//            } else {
//                Log.e("PDFFragment", "Cursor is empty or could not be accessed.")
//            }
//        } ?: run {
//            Log.e("PDFFragment", "Query failed. Cursor is null.")
//        }
//
//        return null
//    }


    private fun convertSizeToDouble(size: String): Double {
        // Convert comma to dot for proper parsing
        val normalizedSize = size.replace(",", ".").replace(" KB", "")
        return normalizedSize.toDouble()
    }

    private fun promptForManageAllFilesAccessPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted, proceed with your operation
                loadPdfFiles()
            }

            else -> {
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                loadPdfFiles()
            } else {
                Log.e("PDFFragment", "Permission denied for reading external storage.")
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        // Lấy ngày và giờ hiện tại
        val now = LocalDateTime.now()

        // Định dạng ngày giờ theo kiểu "yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Chuyển đổi LocalDateTime thành chuỗi định dạng
        return now.format(formatter)
    }
//    override fun onItemClick(item: String) {
//        when (item) {
//            "editName" -> {}
//
//            "delete" -> {}
//            "share" -> {}
//
//        }
//    }


//    private fun getPdfList(): List<PdfFile> {
//        val pdfList: MutableList<PdfFile> = mutableListOf()
//
//        val projection = arrayOf(
//            MediaStore.Files.FileColumns.DATA,
//            MediaStore.Files.FileColumns.DISPLAY_NAME,
//            MediaStore.Files.FileColumns.SIZE,
//            MediaStore.Files.FileColumns.DATE_ADDED
//        )
//
//        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
//        val mimeType = "application/pdf"
//        val selectionArgs = arrayOf(mimeType)
//        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
//
//        val uri: Uri = MediaStore.Files.getContentUri("external")
//
//        requireActivity().contentResolver.query(
//            uri,
//            projection,
//            selection,
//            selectionArgs,
//            sortOrder
//        )?.use { cursor ->
//            if (cursor.moveToFirst()) {
//                val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
//                val nameIndex =
//                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
//                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
//                val dateIndex =
//                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
//
//                val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
//
//                do {
//                    val filePath = cursor.getString(dataIndex) ?: continue
//                    val fileName = cursor.getString(nameIndex) ?: "Unknown"
//                    val fileSizeBytes = cursor.getLong(sizeIndex)
//                    val fileSizeKB = fileSizeBytes / 1024.0
//                    val fileTimeStamp = cursor.getLong(dateIndex)
//                    val fileDate = Date(fileTimeStamp * 1000)
//
//                    val formattedDate = dateFormat.format(fileDate)
//
//                    pdfList.add(
//                        PdfFile(
//                            0,
//                            filePath,
//                            fileName,
//                            String.format("%.2f KB", fileSizeKB),
//                            formattedDate,
//                            false,
//                            ""
//                        )
//                    )
//
//                } while (cursor.moveToNext())
//            } else {
//                Log.e("PDFFragment", "Cursor is empty or could not be accessed.")
//            }
//        } ?: run {
//            Log.e("PDFFragment", "Query failed. Cursor is null.")
//        }
//
//        return pdfList
//    }

    override fun onResume() {
        super.onResume()
        if(viewModel.getValueSort().value.equals("None")){
            sortListPDF()
        }
    }

}
