package com.example.facerecognition.MainActivity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.facerecognition.BitmapUtils
import com.example.facerecognition.Entity.RegisteredFace
import com.example.facerecognition.FaceListActivity
import com.example.facerecognition.FaceRecognitionHelper
import com.example.facerecognition.databinding.ActivityMainBinding
import com.example.facerecognition.dialog.AttendanceConfirmationDialog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    val dialogAttendanceConfirmation = AttendanceConfirmationDialog()
    private var isDialogShown = false
    private var faceRecognitionHelper = FaceRecognitionHelper()
    private var isProcessing = false
    private lateinit var binding: ActivityMainBinding
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    // preview
//    camera
//    camera provider
//    image capture
//    required permision
//    camera excecutir
//
    private var imageCapture: ImageCapture? = null
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider?  = null
    private val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.CAMERA,
    )
    private  lateinit var cameraExcecutor: ExecutorService
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job




// on create akan kerja waktu halaman muncul
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        faceRecognitionHelper = FaceRecognitionHelper()

        cameraExcecutor = Executors.newSingleThreadExecutor()
        // karna kamera kompleks sehingga di buat thread , yang mana thread dianalogikan pabrik dari sebuah produk,karna kompleks kita butuh buat thread yang baru


        // init face recognition helper
        launch(Dispatchers.Default) {
            faceRecognitionHelper.init(this@MainActivity)
        }

        requestPermissions()
        //, oleh sebab  on create aktiv , itu langsung ada rquest permision , biar tahu user kasih izin atau gak.

        binding.addButton.setOnClickListener{
            binding.saveButton.visibility = View.VISIBLE
            binding.nameEditText.visibility = View.VISIBLE
            binding.addButton.visibility = View.GONE
            binding.matricEditText.visibility = View.VISIBLE
            binding.registerFace.visibility = View.VISIBLE
        }

        binding.saveButton.setOnClickListener{
            if (binding.nameEditText.text.toString().isNotEmpty()){
                takePicture()
            }


        }




        binding.nameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideNameEditText()
                if (binding.nameEditText.text.toString().isNotEmpty()) {
                    binding.saveButton.visibility = View.VISIBLE
                } else {
                    binding.saveButton.visibility = View.GONE
                }
                binding.addButton.visibility = View.VISIBLE
            }
        }

        binding.registerFace.setOnFocusChangeListener{ _, hasFocus ->
            if (!hasFocus) {
                hideNameEditText()
                if (binding.nameEditText.text.toString().isNotEmpty()) {
                    binding.saveButton.visibility = View.VISIBLE
                } else {
                    binding.saveButton.visibility = View.GONE
                }
                binding.addButton.visibility = View.VISIBLE
            }
        }


        binding.saveButton.setOnClickListener {
            if (binding.nameEditText.text.toString().isNotEmpty()) {
                takePicture()
                hideSaveButtonAndNameEditText()
            } else{
                hideSaveButtonAndNameEditText()
            }

        }


        binding.studentList.setOnClickListener {
            val intent = Intent(this, FaceListActivity::class.java)
            startActivity(intent)
        }




        binding.saveButton.visibility = View.GONE
        binding.nameEditText.visibility = View.GONE
        binding.registerFace.visibility = View.GONE

        dialogAttendanceConfirmation.setOnTurnOffAttendanceModeListener {
            binding.switchAttendance.isChecked = false
        }




    }

    private fun hideNameEditText() {
        if (binding.nameEditText.text.toString().isEmpty()) {
            binding.nameEditText.visibility = View.GONE
            binding.matricEditText.visibility = View.GONE
            binding.registerFace.visibility = View.GONE
            binding.addButton.visibility = View.VISIBLE
        }
    }

    private fun hideSaveButtonAndNameEditText() {
        binding.saveButton.visibility = View.GONE
        binding.nameEditText.visibility = View.GONE
        binding.addButton.visibility = View.VISIBLE
        binding.matricEditText.visibility = View.GONE
        binding.registerFace.visibility = View.GONE
    }


    private fun showText(name: String) {
       launch(Dispatchers.Main) {
           binding.logTextView.text = name

       }
    }




    private fun requestPermissions() {
        val requestMultiplePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {permissions -> // di dalam kurung kurawal adalah hasil permision daripada user
            // untuk buat objek request permision
            if (allPermissionGranted())
                binding.viewFinder.post{
                    setUpCamera()
                }

            else
                Toast.makeText(
                    this,
                    "IZIN KAMERA DAN LOKASI DIBUTUHKAN",
                    Toast.LENGTH_SHORT
                ).show()


        }

        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.R){
            REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            // android R ada kebijakan berbeda intinya, dimana kita akan nambah permision baru yang namanya write external_storage
        }
        requestMultiplePermission.launch(REQUIRED_PERMISSIONS.toTypedArray())
        //baris 75 hasil dari objek yang dibuat

        binding.switchCameraButton.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            bindCameraUseCases()
        }

    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        // part atas untung nyambungin ke hardware kameranya dan di bawah add listener di sebabkan limitasi hardware yang butuhkan waktu
        cameraProviderFuture.addListener({
            // Camera Provider
            cameraProvider = cameraProviderFuture.get()

            // Build and bind the camera use cases
            bindCameraUseCases()// ini kalimat nyatain ngatur tampilan dari hardware kamera ke app

        },
            ContextCompat.getMainExecutor(this)// menentukan thread yang mengakse camera
            )

    } // kuurng kurawal adalh tempat callback (normalnya).3

    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("camera initialization failed.")

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build() // Update this line

        preview = Preview.Builder()
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build()

        val imageFrameAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(480, 640))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
        imageFrameAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), object : ImageAnalysis.Analyzer {
            override fun analyze(image: ImageProxy) {
                Log.w("VG-CHECK", "frame received")
                if (isProcessing) {
                    image.close()
                    return
                } else {
                    isProcessing = true

                    BitmapUtils.processBitmap(image) {
                        image.close()
                        launch(Dispatchers.Default) {
                            faceRecognitionHelper.recognizerFace(it) {
                                showText(it?.first?.name ?: "Unknown")
                                isProcessing = false
                                Log.w("RAY", "DEteCteD ${it?.first}")
                                it?.let {
                                    showAttendanceConfirmationDialog(it.first, it.second)
                                }
                            }
                        }
                    }
                }
            }
        })

        val imageCaptureBuilder = ImageCapture.Builder()
        imageCapture = imageCaptureBuilder
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageFrameAnalysis
            )
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e("VG-CHECK", "use case binding failed", exc)
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(
            this, it

        ) == PackageManager.PERMISSION_GRANTED
    }
    // bagian ini adalah fungsi untuk nge cek dengan cara looping semua permision yang di butuhkan

    private fun takePicture() {
        val imageCapture = imageCapture ?: return

        val name = "FaceRecognition_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Face_Recognition")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        binding.logTextView.text = "Capturing photo..."
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    binding.logTextView.text = "Okay, you're good"
                    outputFileResults.savedUri?.let {
                        BitmapUtils.getBitmapFromUri(it, this@MainActivity) { bitmap ->
                            launch(Dispatchers.Default) {
                                faceRecognitionHelper.registerFace(
                                    this@MainActivity,
                                    bitmap,
                                    binding.nameEditText.text.toString(),
                                    binding.matricEditText.text.toString()
                                ) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Registered",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@MainActivity,"Please Wait", Toast.LENGTH_LONG).show()
                }



            })
    }

    private fun showAttendanceConfirmationDialog(registeredFace: RegisteredFace, score: Double) {
        if (dialogAttendanceConfirmation.isVisible) return
        if (binding.switchAttendance.isChecked == false) return

        dialogAttendanceConfirmation.registeredFace = registeredFace
        dialogAttendanceConfirmation.score = score
        dialogAttendanceConfirmation.show(supportFragmentManager.beginTransaction(), "DIALOG_CONFIRMATION")
    }


    // Bagian recyclereviewer



}