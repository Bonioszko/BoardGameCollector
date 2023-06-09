package edu.put.inf151892

import android.media.AsyncPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import models.BoardGameDetails
import models.BoardGameDetailsParser
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

import android.provider.MediaStore
import android.widget.Button

import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import edu.put.inf151892.databinding.ActivityGameDetailsBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
typealias LumaListener = (luma: Double) -> Unit
class GameDetailsActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityGameDetailsBinding
    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var text:TextView
    private lateinit var image: ImageView
    private lateinit var numberOfPlayers: TextView
    private lateinit var released: TextView
    private lateinit var playingTime: TextView
    private lateinit var fullImage: ImageView
    private lateinit var prev: Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewBinding = ActivityGameDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        if (allPermissionsGranted()) {
            Log.d("lece", "lec")
            startCamera()
        } else {
            requestPermissions()
        }
        viewBinding.btnAdd.setOnClickListener { takePhoto() }



        cameraExecutor = Executors.newSingleThreadExecutor()
        setupCamera()
        text= findViewById(R.id.titleText)
        image = findViewById(R.id.imageView)
        numberOfPlayers = findViewById(R.id.numberOfPlayers)
        released = findViewById(R.id.released)
        playingTime = findViewById(R.id.playingTime)
        fullImage = findViewById(R.id.imageFullScreen)
        prev = findViewById(R.id.btnPrev)

        val bundle : Bundle? = intent.extras
        val bggId = bundle!!.getInt("bggId")
        val imageBundle = bundle.getString("image")
        val playingTimeInt = bundle.getInt("playingTime")
        val minPlayers = bundle.getInt("minPlayers")
        val maxPlayers = bundle.getInt("maxPlayers")
        val yearPublished = bundle.getInt("yearPublished")
        val name = bundle.getString("name")
        text.text = name
        Glide.with(this).load(imageBundle)
            .apply(RequestOptions()
                .centerCrop())
            .into(image)
        numberOfPlayers.append(minPlayers.toString())
        numberOfPlayers.append(" - ")
        numberOfPlayers.append(maxPlayers.toString())
        released.append(yearPublished.toString())
        playingTime.append(playingTimeInt.toString())
        playingTime.append(" min")
        image.setOnClickListener{
            Log.d("tag","cos")
            Glide.with(this).load(imageBundle)
                .apply(RequestOptions()
                    .centerCrop())
                .into(fullImage)
            fullImage.visibility = View.VISIBLE

        }
        fullImage.setOnClickListener{
            fullImage.visibility = View.GONE
        }

        prev.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
//        CoroutineScope(Dispatchers.Main).launch {
//            val boardGameDetails = fetchBoardGameDetails(bggId.toString())
//            text.text = boardGameDetails.name
//            Glide.with(this@GameDetailsActivity)
//                .load(imageBundle)
//                .apply(RequestOptions().centerCrop())
//                .into(image)
//            numberOfPlayers.append(boardGameDetails.minPlayers)
//            numberOfPlayers.append(" - ")
//            numberOfPlayers.append(boardGameDetails.maxPlayers)
//            released.append(boardGameDetails.yearPublished)
//            playingTime.append(boardGameDetails.playingTime)
//            playingTime.append(" min")
//            // Now you can use the fetched board game details as needed
//            // For example, you can access properties like boardGameDetails.name, boardGameDetails.thumbnail, etc.
//        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            image.setImageURI(imageUri)

        }}
        private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }
    private fun setupCamera() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }
    private fun takePhoto() { // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )}
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    suspend fun fetchBoardGameDetails(gameId: String): BoardGameDetails {
        val boardGameDetailsParser = BoardGameDetailsParser(gameId)
        return boardGameDetailsParser.parse()
    }

}