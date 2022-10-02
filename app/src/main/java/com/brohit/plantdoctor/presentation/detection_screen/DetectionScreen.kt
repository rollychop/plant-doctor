package com.brohit.plantdoctor.presentation.detection_screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun DetectionScreen(
    imageUri: Uri,
    vm: DetectionViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val state = vm.state.value
            if (state.isLoading) {
                CircularProgressIndicator()
            }
            Image(
                modifier = Modifier.fillMaxSize(.7f),
                painter = rememberImagePainter(data = imageUri),
                contentDescription = "cropped image"
            )
            if (state.error.isNotEmpty()) {
                Text(text = state.error, color = MaterialTheme.colors.error)
            }
            Row {
                Button(
                    onClick = {
                        vm.predict(imageUri)
                    },
                    enabled = state.isLoading.not()
                ) {
                    Text(text = "Detect")
                }
            }
        }
    }

}