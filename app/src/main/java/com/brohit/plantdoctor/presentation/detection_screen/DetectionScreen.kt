package com.brohit.plantdoctor.presentation.detection_screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.presentation.component.JetsnackButton
import com.brohit.plantdoctor.presentation.component.SnackItem
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
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
        val state = vm.state.value

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.plant == null) {
                Image(
                    modifier = Modifier.fillMaxSize(.7f),
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "cropped image"
                )
                Row {
                    JetsnackButton(
                        onClick = {
                            vm.predict(imageUri)
                        },
                        enabled = state.isLoading.not()
                    ) {
                        Text(text = "Detect")
                    }
                }
            }
            if (state.error.isNotEmpty()) {
                Text(
                    text = state.error, color = PlantDoctorTheme.colors.error
                )
            }
            if (state.plant != null) {
                SnackItem(plant = state.plant, onSnackClick = {})
                Text(text = "State : ${state.plant.diseaseName}")
            }


        }
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
            )
        }

    }

}