package com.brohit.plantdoctor.presentation.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lens
import androidx.compose.material.icons.sharp.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.brohit.plantdoctor.common.IN
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


private const val TAG = "CameraView"
private fun takePhoto(
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    filenameFormat: String = "yyyy-MM-dd-HH-mm-ss-SSS"
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(
            filenameFormat,
            IN
        ).format(System.currentTimeMillis()).plus(".jpg")
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e(TAG, "Take photo error:", exception)
            onError(exception)
        }


        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.run { resume(cameraProvider.get()) }
            }, ContextCompat.getMainExecutor(this))
        }
    }


@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    onClose: (() -> Unit)? = null
) {
    // 1
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder()
        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .build()
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder()
            .setTargetResolution(Size(480, 640))
//            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
    }
    /*val imageAnalysis: ImageAnalysis = remember {
        ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(previewView.display.rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()

    }*/
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()
    var imageIsSaving by remember {
        mutableStateOf(false)
    }

    // 2
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
//            imageAnalysis
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    // 3

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { previewView }
        )

        IconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = { onClose?.invoke() }) {
            Icon(
                imageVector = Icons.Filled.Close, contentDescription = "Close Camera",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Sharp.Image,
                    contentDescription = "Select Crop Image",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
            IconButton(
                onClick = {
                    imageIsSaving = true
                    takePhoto(imageCapture, outputDirectory, executor,
                        onImageCaptured = { uri ->
                            imageIsSaving = false
                            context.mainExecutor.execute {
                                onImageCaptured(uri)
                            }
                        }, onError = { imageCaptureException ->
                            imageIsSaving = false
                            context.mainExecutor.execute {
                                onError(imageCaptureException)
                            }
                        })
                },
                modifier = Modifier
                    .padding(20.dp),
                enabled = imageIsSaving.not(),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Lens,
                    contentDescription = "Take picture",
                    tint = Color.White,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
                if (imageIsSaving)
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = Color.White,
                    )

            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "Info Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }

    }
}