package com.example.matheasyapp.view.home


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.matheasyapp.databinding.FragmentPictureBinding
import com.example.matheasyapp.databinding.LayoutAccessPicturBinding
import com.example.matheasyapp.view.home.itf.OnItemClickListenerPicture
import com.example.matheasyapp.view.picture.adapter.ImageAdapter
import com.example.matheasyapp.view.picture.CropImageActivity
import com.example.matheasyapp.view.picture.FolderActivity
import java.io.File

class PictureFragment : Fragment(), OnItemClickListenerPicture {

    private lateinit var folderMap: MutableMap<String, MutableList<String>>
    private lateinit var allImagesList: MutableList<String>
    private lateinit var binding: FragmentPictureBinding

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

        openSettingsLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                checkAndRequestPermissions()
            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    binding.layoutContentImg.visibility = View.VISIBLE
                    binding.layoutRequestImg.visibility = View.GONE
                    loadAllImages()
                } else {
                    binding.layoutContentImg.visibility = View.GONE
                    binding.layoutRequestImg.visibility = View.VISIBLE
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureBinding.inflate(inflater, container, false)

        checkAndRequestPermissions()

        binding.btnAccessPicture.setOnClickListener {
            openAppSettings()
        }

        return binding.root
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", requireContext().packageName, null)
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
                requireContext(),
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
        val cursor = requireActivity().contentResolver.query(
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

        folderMap["All"] = allImagesList

        displayImagesInFolder("All")

        val folderList = folderMap.keys.toList()

        binding.layoutFolder.setOnClickListener {
            val intent = Intent(requireContext(), FolderActivity::class.java)
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
        val imageAdapter = ImageAdapter(requireContext(), selectedImages, this)
        binding.gridView.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(image: String) {
        val intent = Intent(requireActivity(), CropImageActivity::class.java)
        intent.putExtra("img_path", image)
        startActivity(intent)
    }
}