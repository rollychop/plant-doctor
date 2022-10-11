package com.brohit.plantdoctor.presentation.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options


@Composable
fun ImageSelectorAndCropper() {
    var imageUri by remember {
        mutableStateOf(Uri.EMPTY)
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
    if (imageUri == Uri.EMPTY) {
        Button(onClick = {
            imageCropLauncher.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setAspectRatio(1, 1)
                }
            )
        }) {
            Text(text = "Open Camera")
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(160.dp)
                    .height(160.dp),
                painter = rememberImagePainter(data = imageUri),
                contentDescription = null
            )
            Row {
                Button(onClick = { imageUri = Uri.EMPTY }) {
                    Text(text = "Retake")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Detect")
                }
            }

        }
    }
}