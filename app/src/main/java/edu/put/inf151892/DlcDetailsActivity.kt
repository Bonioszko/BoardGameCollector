package edu.put.inf151892

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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
import androidx.core.content.ContextCompat
import edu.put.inf151892.databinding.ActivityGameDetailsBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector

import androidx.camera.core.ImageCaptureException
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.util.Locale


class DlcDetailsActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityGameDetailsBinding
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var text:TextView
    private lateinit var image: ImageView
    private lateinit var numberOfPlayers: TextView
    private lateinit var released: TextView
    private lateinit var playingTime: TextView
    private lateinit var fullImage: ImageView
    private lateinit var prev: Button
    private lateinit var next : Button
    private lateinit var del:Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    var db = DBHandler(this)
    private lateinit var images: MutableList <String>

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
        text= findViewById(R.id.titleText)
        image = findViewById(R.id.imageView)
        numberOfPlayers = findViewById(R.id.numberOfPlayers)
        released = findViewById(R.id.released)
        playingTime = findViewById(R.id.playingTime)
        fullImage = findViewById(R.id.imageFullScreen)
        prev = findViewById(R.id.btnPrev)
        del = findViewById(R.id.btnDelete)
        next = findViewById(R.id.btnNext)

        val bundle : Bundle? = intent.extras
        val bggId = bundle!!.getInt("bggId")
        val imageBundle = bundle.getString("image")
        val playingTimeInt = bundle.getInt("playingTime")
        val minPlayers = bundle.getInt("minPlayers")
        val maxPlayers = bundle.getInt("maxPlayers")
        val yearPublished = bundle.getInt("yearPublished")
        val name = bundle.getString("name")
        var current = 0

        viewBinding.btnAdd.setOnClickListener { takePhoto(bggId) }
        cameraExecutor = Executors.newSingleThreadExecutor()
        setupCamera()
        images = mutableListOf(" ")
        next.setOnClickListener {

            current += 1
            Log.d("current", current.toString())
            if (current >= images.size) {
                current = 0 // Reset to the first image if it exceeds the list size
            }

            if (current == 0) {
                Glide.with(this).load(imageBundle)
                    .apply(RequestOptions().centerCrop())
                    .into(image)
                Glide.with(this).load(imageBundle)
                    .apply(RequestOptions().centerCrop())
                    .into(fullImage)
            } else {
                val imageCurrent = images[current]
                image.setImageURI(imageCurrent.toUri())
                fullImage.setImageURI(imageCurrent.toUri())
            }
        }
        prev.setOnClickListener {

            current -= 1
            Log.d("currentprev", current.toString())
            if (current < 0) {
                current = images.size - 1 // Set the index to the last image if it becomes negative
            }

            if (current == 0) {
                Glide.with(this).load(imageBundle)
                    .apply(RequestOptions().centerCrop())
                    .into(image)
                Glide.with(this).load(imageBundle)
                    .apply(RequestOptions().centerCrop())
                    .into(fullImage)
            } else {
                val imageCurrent = images[current]
                image.setImageURI(imageCurrent.toUri())
                fullImage.setImageURI(imageCurrent.toUri())
            }
        }
//
        text.text = name
        Glide.with(this).load(imageBundle)
            .apply(RequestOptions()
                .centerCrop())
            .into(image)
        Glide.with(this).load(imageBundle)
            .apply(RequestOptions()
                .centerCrop())
            .into(fullImage)
        numberOfPlayers.append(minPlayers.toString())
        numberOfPlayers.append(" - ")
        numberOfPlayers.append(maxPlayers.toString())
        released.append(yearPublished.toString())
        playingTime.append(playingTimeInt.toString())
        playingTime.append(" min")
        del.setOnClickListener{
            db.deleteImagesforGame(bggId)
            Glide.with(this).load(imageBundle)
                .apply(RequestOptions()
                    .centerCrop())
                .into(image)
            Glide.with(this).load(imageBundle)
                .apply(RequestOptions()
                    .centerCrop())
                .into(fullImage)
            images = mutableListOf(" ")
            current =0
        }
        image.setOnClickListener{
            Log.d("tag","cos")

            fullImage.visibility = View.VISIBLE

        }
        fullImage.setOnClickListener{
            fullImage.visibility = View.GONE
        }


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
            viewBinding.viewFinder.visibility= View.GONE
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
    private fun takePhoto(bggId: Int) { // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        viewBinding.viewFinder.visibility = View.VISIBLE
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
                    val uri = output.savedUri
                    db.addImage(bggId, uri.toString())
                    images = db.getImages(bggId)
                    images.add(0,"")
                    image.setImageURI(uri)
                    fullImage.setImageURI(uri)
                    Log.d(TAG, msg)
                    viewBinding.viewFinder.visibility= View.GONE
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




}