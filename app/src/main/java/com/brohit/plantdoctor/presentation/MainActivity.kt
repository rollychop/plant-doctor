package com.brohit.plantdoctor.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.R
import com.brohit.plantdoctor.presentation.camera.CameraView
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var photoUri: Uri
    private val shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)

    private val shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)

    private val shouldShowDialog: MutableState<Boolean> = mutableStateOf(false)
    private val showCamera: MutableState<Boolean> = mutableStateOf(false)


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i(TAG, "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.i(TAG, "Permission denied")
        }
    }

    private fun requestCameraPermission(showDialog: (() -> Unit)? = null) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i(TAG, "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                Log.i(TAG, "Show camera permissions dialog")
                showDialog?.invoke()
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantDoctorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
//                    DestinationsNavHost(navGraph = NavGraphs.root)

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (shouldShowDialog.value) {
                            Button(onClick = {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                                shouldShowDialog.value = false
                            }) {
                                Text(text = "Camera Permission is required to proceed")
                            }
                        }
                        if (shouldShowCamera.value) {
                            Button(onClick = { showCamera.value = true }) {
                                Text(text = "Open Camera")
                            }
                            if (showCamera.value) {
                                CameraView(
                                    outputDirectory = outputDirectory,
                                    executor = cameraExecutor,
                                    onImageCaptured = { handleImageCapture(it) },
                                    onError = {}
                                )

                            }
                        }
                        if (shouldShowPhoto.value) {
                            Image(
                                painter = rememberImagePainter(photoUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(160.dp)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(160.dp)),
                                contentScale = ContentScale.FillWidth
                            )
                            Button(onClick = { }) {
                                Text(text = "Detect")
                            }
                        }
                    }
                }
            }
        }
        requestCameraPermission {
            shouldShowDialog.value = true
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private fun handleImageCapture(uri: Uri) {
        Log.i(TAG, "Image captured: $uri")
        shouldShowCamera.value = false
        showCamera.value = false
        photoUri = uri
        shouldShowPhoto.value = true
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
