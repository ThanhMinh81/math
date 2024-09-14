package com.example.matheasyapp.view.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.matheasyapp.databinding.ActivityPictureBinding
import com.example.matheasyapp.view.home.itf.OnItemClickListenerPicture
import com.example.matheasyapp.view.picture.CropImageActivity
import com.example.matheasyapp.view.picture.FolderActivity
import com.example.matheasyapp.view.picture.adapter.ImageAdapter
import java.io.File

class PictureActivity : AppCompatActivity(), OnItemClickListenerPicture {

    private lateinit var folderMap: MutableMap<String, MutableList<String>>
    private lateinit var allImagesList: MutableList<String>
    private lateinit var binding: ActivityPictureBinding

    private lateinit var openSettingsLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val folderActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedFolder = result.data?.getStringExtra("selectedFolder")
            selectedFolder?.let {
                displayImagesInFolder(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo ActivityResultLauncher để yêu cầu quyền
        openSettingsLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                checkAndRequestPermissions()
            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    binding.layoutContentImg.visibility = View.VISIBLE
                    binding.layoutRequestImg.visibility = View.GONE

                    // Quyền đã được cấp, gọi hàm loadAllImages để tải ảnh
                    loadAllImages()
                } else {

                    binding.layoutContentImg.visibility = View.GONE
                    binding.layoutRequestImg.visibility = View.VISIBLE
                    // Tu choi quyen
                }
            }

        // Kiểm tra và yêu cầu quyền truy cập
        checkAndRequestPermissions()

        binding.icBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.btnAccessPicture.setOnClickListener {
            openAppSettings()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", packageName, null)
        }
        openSettingsLauncher.launch(intent)
    }

    private fun checkAndRequestPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                binding.layoutRequestImg.visibility = View.GONE
                binding.layoutContentImg.visibility = View.VISIBLE
                loadAllImages()
            }

            else -> {
                binding.layoutRequestImg.visibility = View.VISIBLE
                binding.layoutContentImg.visibility = View.GONE
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun loadAllImages() {
        folderMap = mutableMapOf()
        allImagesList = mutableListOf()

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(dataColumn)
                val folderPath = File(imagePath).parent

                if (folderPath != null) {
                    val folderName = File(folderPath).name
                    if (folderName !in folderMap) {
                        folderMap[folderName] = mutableListOf()
                    }
                    folderMap[folderName]?.add(imagePath)
                    allImagesList.add(imagePath)
                }
            }
        }

        // Thêm tất cả các ảnh vào mục "All"
        folderMap["All"] = allImagesList

        // Hiển thị tất cả hình ảnh khi mở màn hình
        displayImagesInFolder("All")

        val folderList = folderMap.keys.toList()
        val extendedFolderList = listOf("All") + folderList

        binding.layoutFolder.setOnClickListener {
            val intent = Intent(this, FolderActivity::class.java)
            intent.putExtra("folderMap", HashMap(folderMap))
            folderActivityResultLauncher.launch(intent)
        }
    }

    private fun displayImagesInFolder(folderName: String) {
        binding.titleFolder.text = folderName
        val selectedImages = if (folderName == "All") {
            allImagesList
        } else {
            folderMap[folderName] ?: emptyList()
        }
        val imageAdapter = ImageAdapter(this, selectedImages, this)
        binding.gridView.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged() // Cập nhật giao diện
    }

    override fun onItemClick(image: String) {
        val intent = Intent(this, CropImageActivity::class.java)
        intent.putExtra("img_path", image)
        startActivity(intent)
    }
}
