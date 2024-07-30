package com.example.matheasyapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.Manifest
import android.graphics.BitmapFactory
import android.util.Log
import android.util.Rational
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.camera.view.PreviewView
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.google.common.util.concurrent.ListenableFuture

class CameraFragment : Fragment() {

    private lateinit var iView: View
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    private lateinit var btnCapture: ImageView

    private lateinit var cameraControl: CameraControl
    private lateinit var cameraInfo: CameraInfo

    private lateinit var rgZoomCam: RadioGroup;

    lateinit var rb1f: RadioButton
    lateinit var rb2f: RadioButton
    lateinit var rb3f: RadioButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iView = inflater.inflate(R.layout.fragment_camera, container, false)

        previewView = iView.findViewById(R.id.view_finder)
        rgZoomCam = iView.findViewById(R.id.rgZoomCamera)
        rb1f = iView.findViewById(R.id.rb1F)
        rb2f = iView.findViewById(R.id.rb2F)
        rb3f = iView.findViewById(R.id.rb3F)



        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        btnCapture = iView.findViewById(R.id.btnCapure)


        rb1f.isChecked = true
        rb1f.text = "1x"


        rgZoomCam.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.rb1F -> {
                    zoomCamera(1.5f)
                    rb1f.text = "1x"
                    rb2f.text = "2"
                    rb3f.text = "3"
                }

                R.id.rb2F -> {
                    zoomCamera(2f)
                    rb2f.text = "2x"
                    rb1f.text = "1"
                    rb3f.text = "3"
                }

                R.id.rb3F -> {
                    zoomCamera(2.5f)
                    rb3f.text = "3x"
                    rb1f.text = "1"
                    rb2f.text = "2"
                }
            }
        }



        btnCapture.setOnClickListener {
            takePhoto()
        }

        initCamera();

        return iView
    }

    private fun initCamera() {

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        bindCameraUseCases(cameraProvider)
                    }, ContextCompat.getMainExecutor(requireActivity()))
                } else {

                }
            }

        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
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

    private fun takePhoto() {

        val photoFile =
            File(requireActivity().externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("checkkkk", "Photo saved successfully: ${photoFile.absolutePath}")
//                    displayPhoto(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("checkkkk", "Error saving photo: ${exception.message}", exception)
                }
            })

    }


    private fun bindCameraUseCases(cameraProvider: ProcessCameraProvider) {

        val preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        imageCapture = ImageCapture.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        // Gỡ bỏ tất cả các use case đang liên kết và liên kết lại
        cameraProvider.unbindAll()
        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        // Initialize cameraControl and cameraInfo
        cameraControl = camera.cameraControl
        cameraInfo = camera.cameraInfo

    }


    private fun zoomCamera(scaleFactor: Float) {
        // Ensure that cameraInfo and cameraControl are initialized
        val currentZoomState = cameraInfo.zoomState.value ?: return
        val zoomRatio = currentZoomState.zoomRatio * scaleFactor
        cameraControl.setZoomRatio(scaleFactor)
    }




}


