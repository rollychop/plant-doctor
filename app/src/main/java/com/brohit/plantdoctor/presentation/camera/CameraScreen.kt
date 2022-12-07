@file:OptIn(ExperimentalPermissionsApi::class)

package com.brohit.plantdoctor.presentation.camera

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.sharp.CameraEnhance
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.BuildConfig
import com.brohit.plantdoctor.R
import com.brohit.plantdoctor.common.Constants
import com.brohit.plantdoctor.presentation.camera.CameraPermissionStatus.*
import com.brohit.plantdoctor.presentation.component.PdButton
import com.brohit.plantdoctor.presentation.component.PdCard
import com.brohit.plantdoctor.presentation.component.PdSurface
import com.brohit.plantdoctor.presentation.destinations.DetectionScreenDestination
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.io.*
import java.util.*


private const val TAG = "CameraScreen"

@Destination
@Composable
fun CameraScreen(
    viewModel: CameraScreenViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    var imageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }


    PdSurface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = Constants.IMG_URL
                ), contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(PlantDoctorTheme.colors.uiBackground.copy(alpha = .2f))
            ) {

                Column(
                    modifier = Modifier.fillMaxHeight(0.4f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(32.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(50))
                            .background(
                                PlantDoctorTheme.colors.uiBackground.copy(alpha = .4f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = Icons.Sharp.CameraEnhance,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(.6f)
                        )

                    }
                }
            }

            var showCameraPermission by remember {
                mutableStateOf(false)
            }
            val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
            var showCameraView by remember {
                mutableStateOf(false)
            }
            var showAlert by remember {
                mutableStateOf(false)
            }
            var showAlertPermanent by remember {
                mutableStateOf(false)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight(.6f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(PlantDoctorTheme.colors.uiBackground),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                IconSelector(
                    caption = "Camera",
                    icon = Icons.Outlined.Camera,
                    onClick = {
                        if (permissionState.status.isGranted) {
                            showCameraView = true
                        } else {
                            showCameraPermission = true
                        }
                    }
                )
                val ctx = LocalContext.current
                val imageSelectorResult = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = {
                        val bitmap =
                            ImageDecoder.decodeBitmap(
                                ImageDecoder.createSource(
                                    ctx.contentResolver,
                                    it!!
                                )
                            )
                        val file = saveBitmap(
                            bitmap,
                            File(viewModel.getOutputDirectory(ctx as ContextWrapper), "d.jpeg")
                        )
                        imageUri = file.toUri()


                    }
                )
                IconSelector(
                    caption = "Gallery",
                    icon = Icons.Outlined.Image,
                    onClick = {
                        imageSelectorResult.launch("image/*")
                    }
                )

                if (imageUri != Uri.EMPTY) {
                    navigator.navigate(DetectionScreenDestination(imageUri = imageUri))
                }
                if (showCameraPermission) {
                    CameraWithPermission { status ->
                        when (status) {
                            GRANTED -> {
                                showAlert = false
                                showAlertPermanent = false
                                showCameraView = true
                            }
                            SEMI_DENINED -> {
                                showAlert = true
                                showAlertPermanent = false

                            }
                            DENIED_PERMANTLY -> {
                                showAlertPermanent = true
                                showAlert = false
                            }
                        }
                        showCameraPermission = false
                    }
                }
                if (showAlertPermanent) {
                    CustomAlertDialog(text = buildAnnotatedString {
                        append(
                            "Inorder to predict plant leaf status this app requires camara permission. " +
                                    "Please goto app setting and allow camera permission for "
                        )
                        pushStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        append(stringResource(id = R.string.app_name))
                        append(".")

                    }, onDismiss = {
                        showAlertPermanent = false
                    })
                }
                if (showAlert) {
                    CustomAlertDialog(
                        buildAnnotatedString { append("App Needs Camera permission to predict plant leaf status.") },
                        onDismiss = {
                            showAlert = false
                        })
                }


            }
            val context = LocalContext.current as ContextWrapper
            if (showCameraView)
                CameraView(
                    modifier = Modifier.fillMaxSize(),
                    outputDirectory = viewModel.getOutputDirectory(context),
                    executor = viewModel.getCameraExecutor(),
                    onImageCaptured = { navigator.navigate(DetectionScreenDestination(it).route) },
                    onError = ::onImageCaptureException,
                    onClose = {
                        showCameraView = false
                    }
                )

        }

    }

}

@Composable
fun CustomAlertDialog(text: AnnotatedString, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = PlantDoctorTheme.colors.uiBackground,

        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                PdButton(
                    onClick = onDismiss,
                    backgroundGradient = PlantDoctorTheme.colors.gradient2_3
                ) {
                    Text(text = "Close")
                }
            }
        },
        title = {
            Text(text = "Permission is Needed !")
        },
        text = {
            Text(
                text = text
            )
        }
    )
}


@Composable
fun CameraWithPermission(
    onLaunch: @Composable (CameraPermissionStatus) -> Unit
) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val eventObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                permissionState.launchPermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(eventObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(eventObserver)
        }
    })
    when {
        permissionState.status.isGranted -> {
            onLaunch(GRANTED)
            Log.d(TAG, "CameraWithPermission: GRANTED")
        }
        permissionState.status.shouldShowRationale -> {
            onLaunch(SEMI_DENINED)
            Log.d(TAG, "CameraWithPermission: SEMI")
        }
        (permissionState.status.isGranted || permissionState.status.shouldShowRationale).not() -> {
            onLaunch(DENIED_PERMANTLY)
            Log.d(TAG, "CameraWithPermission: PERMANENT")
        }
    }
}

enum class CameraPermissionStatus {
    GRANTED, SEMI_DENINED, DENIED_PERMANTLY
}


private fun onImageCaptureException(imageCaptureException: ImageCaptureException) {
    Log.e(TAG, "onImageCaptureException: ", imageCaptureException)
}

private fun getTmpFileUri(context: Context): Uri {
    val tmpFile = File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
}

@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    caption: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        PdCard(elevation = 8.dp) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10))
                    .background(PlantDoctorTheme.colors.iconPrimary),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onClick,
//                modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(.7f),
                        tint = PlantDoctorTheme.colors.uiBackground
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = caption)
    }
}

fun saveFile(sourceFilename: String, destinationFilename: File) {
    var bis: BufferedInputStream? = null
    var bos: BufferedOutputStream? = null
    try {
        bis = BufferedInputStream(FileInputStream(sourceFilename))
        bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
        val buf = ByteArray(1024)
        bis.read(buf)
        do {
            Log.d(TAG, "saveFile: ${buf.contentToString()}")
            bos.write(buf)
        } while (bis.read(buf) != -1)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            bis?.close()
            bos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun saveBitmap(bmp: Bitmap, file: File): File {
    val bytes = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
    file.createNewFile()
    FileOutputStream(file).use { fo ->
        fo.write(bytes.toByteArray())
    }
    return file
}

