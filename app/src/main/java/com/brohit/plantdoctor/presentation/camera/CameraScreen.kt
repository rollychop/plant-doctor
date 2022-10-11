package com.brohit.plantdoctor.presentation.camera

import android.content.ContextWrapper
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.common.Constants
import com.brohit.plantdoctor.presentation.component.JetsnackCard
import com.brohit.plantdoctor.presentation.component.PlantDoctorSurface
import com.brohit.plantdoctor.presentation.destinations.DetectionScreenDestination
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun CameraScreen(
    viewModel: CameraScreenViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    var imageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var showCamera by remember {
        mutableStateOf(false)
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->

        if (result.isSuccessful) {
            // use the cropped image
            imageUri = result.uriContent

        } else {
            // an error occurred cropping
            val exception = result.error
        }
    }
    PlantDoctorSurface(
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
                    icon = Icons.Outlined.Camera
                ) {
                    showCamera = true

                }
                IconSelector(
                    caption = if (showCamera) "Close" else "Gallery",
                    icon = Icons.Outlined.Image
                ) {
                    showCamera = false
                    imageCropLauncher.launch(
                        options {
                            setGuidelines(CropImageView.Guidelines.ON)
                            setAspectRatio(1, 1)
                        }
                    )
                }

                if (imageUri != Uri.EMPTY) {
                    navigator.navigate(DetectionScreenDestination(imageUri = imageUri))
                }


            }
            val context = LocalContext.current as ContextWrapper
            if (showCamera)
                CameraView(
                    modifier = Modifier.fillMaxSize(),
                    outputDirectory = viewModel.getOutputDirectory(context),
                    executor = viewModel.getCameraExecutor(),
                    onImageCaptured = { navigator.navigate(DetectionScreenDestination(it).route) },
                    onError = ::onImageCaptureException
                )

        }

    }

}

private fun onImageCapture(uri: Uri) {

}

private fun onImageCaptureException(imageCaptureException: ImageCaptureException) {

}


@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    caption: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        JetsnackCard(elevation = 8.dp) {

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

