package com.example.matheasyapp.view.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.matheasyapp.R
import com.example.matheasyapp.broadcast.BroadcastReceiverInternet
import com.example.matheasyapp.broadcast.registerNetworkReceiver
import com.example.matheasyapp.broadcast.unregisterNetworkReceiver
import com.example.matheasyapp.databinding.FragmentCameraBinding
import com.google.common.util.concurrent.ListenableFuture
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var photoFile: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture

    private lateinit var cameraControl: CameraControl
    private lateinit var cameraInfo: CameraInfo

    lateinit var binding: FragmentCameraBinding

    private var selectedImage: Boolean = false

    private var dialog: Dialog? = null

    private var isFlashOn = false

    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null

    // handle connect
    private var networkReceiver: BroadcastReceiverInternet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCameraBinding.inflate(inflater, container, false)

        selectedImage = false

        binding.imgSelected.visibility = View.GONE

        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        binding.rb1F.isChecked = true
        binding.rb1F.text = "1x"

        setUpViewInit()

        handleConnect()

        onClick()

        initCamera()

        return binding.root
    }

    private fun setUpViewInit() {
        if (!selectedImage) {
            binding.selectedImage.setImageResource(R.drawable.ic_libary)
            binding.btnCapure.setImageResource(R.drawable.cap_picture)
        }
    }

    private fun handleConnect() {
        // Đăng ký BroadcastReceiver để theo dõi thay đổi kết nối mạng
        networkReceiver = registerNetworkReceiver(requireContext()) { isConnected ->
            // Xử lý sự thay đổi kết nối mạng
            if (!isConnected) {
                showInputDialog(true)
            } else {
                showInputDialog(false)
            }
        }
    }

    private fun onClick() {
        binding.rgZoomCamera.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {

                R.id.rb1F -> {
                    zoomCamera(1.5f)
                    binding.rb1F.text = "1x"
                    binding.rb2F.text = "2x"
                    binding.rb3F.text = "3x"
                }

                R.id.rb2F -> {
                    zoomCamera(2f)
                    binding.rb2F.text = "2x"
                    binding.rb1F.text = "1x"
                    binding.rb3F.text = "3x"
                }

                R.id.rb3F -> {
                    zoomCamera(2.5f)
                    binding.rb3F.text = "3x"
                    binding.rb1F.text = "1x"
                    binding.rb2F.text = "2x"
                }

            }
        }

        binding.btnCapure.setOnClickListener {
            if (!selectedImage) {
                takePhoto()
            } else {
                if (isInternetConnected()) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            selectedImage = false
                            val intent = Intent(requireActivity(), ResponseActivity::class.java)
                            intent.putExtra("photo_file_path", photoFile.absolutePath)
                            intent.putExtra("rotage", photoFile.absolutePath)
                            startActivity(intent)
                        }
                    }
                } else {
                    showDialogNotification()
                }
            }
        }

        binding.selectedImage.setOnClickListener {
            if (selectedImage) {
                binding.imgSelected.visibility = View.GONE
                binding.preview.visibility = View.VISIBLE
                binding.selectedImage.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_libary))
                binding.btnCapure.setImageDrawable(requireActivity().getDrawable(R.drawable.cap_picture))
                selectedImage = false
            } else {
                val intern: Intent = Intent(requireActivity(), PictureActivity::class.java)
                startActivity(intern)
            }
        }

        binding.imgFlash.setOnClickListener { toggleFlashlight() }

    }

    private fun showDialogNotification() {


        var dialogNotifi: Dialog = Dialog(requireContext())

        dialogNotifi.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialogNotifi.setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
        dialogNotifi.setContentView(R.layout.layout_dialog_notifi)  // Đặt nội dung sau khi gọi requestWindowFeature

        dialogNotifi.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnClose = dialogNotifi.findViewById<LinearLayout>(R.id.layoutClose)

        btnClose.setOnClickListener {
            dialogNotifi.dismiss()
        }

        dialogNotifi.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )

        val layoutParams = dialogNotifi.window?.attributes
        layoutParams?.gravity = Gravity.CENTER

        dialogNotifi.window?.attributes = layoutParams
        dialogNotifi.show()

    }


    private fun initCamera() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        bindCameraUseCases(cameraProvider)
                    }, ContextCompat.getMainExecutor(requireActivity()))

                    // Initialize CameraManager
                    cameraManager =
                        requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    cameraId = cameraManager.cameraIdList.firstOrNull()

                } else {
                    // Handle permission denied case
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    bindCameraUseCases(cameraProvider)
                }, ContextCompat.getMainExecutor(requireActivity()))
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }


//    private fun initCamera() {
//        requestPermissionLauncher =
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//                if (isGranted) {
//                    cameraProviderFuture.addListener({
//                        val cameraProvider = cameraProviderFuture.get()
//                        bindCameraUseCases(cameraProvider)
//                    }, ContextCompat.getMainExecutor(requireActivity()))
//                } else {
//
//                    // Handle permission denied case
//                }
//            }
//
//        when {
//            ContextCompat.checkSelfPermission(
//                requireActivity(), Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                cameraProviderFuture.addListener({
//                    val cameraProvider = cameraProviderFuture.get()
//                    bindCameraUseCases(cameraProvider)
//                }, ContextCompat.getMainExecutor(requireActivity()))
//            }
//
//            else -> {
//                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//            }
//        }
//    }

    private fun takePhoto() {
        photoFile =
            File(requireActivity().externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("848484884", "Photo saved successfully: ${photoFile.absolutePath}")
                    displayPhoto(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("checkkkk", "Error saving photo: ${exception.message}", exception)
                }
            })
    }

    private fun displayPhoto(photoFile: File) {
        selectedImage = true
        if (selectedImage) {
            Log.d("45035023453572", "534575025")
            binding.preview.visibility = View.GONE
            binding.imgSelected.visibility = View.VISIBLE

            binding.btnCapure.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_check_large))
            binding.selectedImage.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_close))

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val rotatedBitmap = ExifUtil.getRotatedBitmap(photoFile)
                    saveBitmapToFile(rotatedBitmap, photoFile)
                }

                withContext(Dispatchers.Main) {
                    Picasso.get().load(photoFile).placeholder(R.drawable.bglack)
                        .into(binding.imgSelected)
                }
            }
        }
    }

    private fun zoomCamera(scaleFactor: Float) {
        val currentZoomState = cameraInfo.zoomState.value ?: return
        val zoomRatio = currentZoomState.zoomRatio * scaleFactor
        cameraControl.setZoomRatio(zoomRatio)
    }

    private fun bindCameraUseCases(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build().also {
            it.setSurfaceProvider(binding.preview.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        // Unbind all use cases and bind new ones
        cameraProvider.unbindAll()
        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        // Initialize cameraControl and cameraInfo
        cameraControl = camera.cameraControl
        cameraInfo = camera.cameraInfo
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
    }

    fun isInternetConnected(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }


    fun showInputDialog(show: Boolean) {
        if (show) {
            if (dialog == null || !dialog!!.isShowing) {
                // Chỉ tạo mới dialog nếu nó chưa được tạo hoặc không đang hiển thị
                dialog = Dialog(requireContext())
                dialog?.apply {
                    // Gọi requestWindowFeature trước khi thêm content
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(true) // Cho phép ẩn Dialog khi bấm ra ngoài
                    setContentView(R.layout.dialog_check_connect)  // Đặt nội dung sau khi gọi requestWindowFeature
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    val btnRefresh: Button = findViewById(R.id.btnRefresh)
                    btnRefresh.setOnClickListener {
                        val checkConnect = isInternetConnected()

                        if (checkConnect) {
                            dismiss()
                        } else {
                            blinkDialog(this)
                        }
                    }

                    window?.setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )

                    val layoutParams = window?.attributes
                    layoutParams?.gravity = Gravity.CENTER
                    window?.attributes = layoutParams

                    show()
                }
            }
        } else {
            dialog?.dismiss()
            dialog = null
        }
    }

    private fun blinkDialog(dialog: Dialog) {
        val handler = Handler()
        val blinkDuration = 200L // Thời gian nhấp nháy 200ms
        val blinkInterval = 200L // Thời gian nhấp nháy giữa các lần ẩn hiện (200ms)

        val showRunnable = object : Runnable {
            override fun run() {
                dialog.window?.decorView?.let {
                    if (it.visibility == View.VISIBLE) {
                        it.visibility = View.INVISIBLE
                    } else {
                        it.visibility = View.VISIBLE
                    }
                    handler.postDelayed(this, blinkInterval)
                }
            }
        }

        // Bắt đầu nhấp nháy
        handler.post(showRunnable)

        // Dừng hiệu ứng nhấp nháy sau 500ms
        handler.postDelayed({
            dialog.window?.decorView?.visibility = View.VISIBLE
            handler.removeCallbacks(showRunnable)
        }, blinkDuration)
    }


    override fun onStop() {
        super.onStop()
        selectedImage = false
    }

    override fun onResume() {
        Log.d("45245235235", "fsdihaosdfa")
        super.onResume()
        selectedImage = false
        binding.imgSelected.visibility = View.GONE
        binding.preview.visibility = View.VISIBLE
        binding.selectedImage.setImageResource(R.drawable.ic_libary)
        binding.btnCapure.setImageResource(R.drawable.cap_picture)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver khi Activity bị hủy
        if (networkReceiver != null) {
            unregisterNetworkReceiver(requireContext(), networkReceiver!!)
        }
    }

    private fun toggleFlashlight() {
        try {
            cameraId?.let { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val flashAvailable =
                    characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

                if (flashAvailable) {
                    if (isFlashOn) {
                        cameraManager.setTorchMode(id, false)
                        isFlashOn = false
                    } else {
                        cameraManager.setTorchMode(id, true)
                        isFlashOn = true
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Flashlight not supported on this device",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                Log.e("CameraFragment", "Camera ID is null")
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error accessing camera", Toast.LENGTH_SHORT).show()
        }
    }


}





