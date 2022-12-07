package com.brohit.plantdoctor.presentation.detection_screen

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.domain.model.SnackbarManager
import com.brohit.plantdoctor.presentation.component.PdButton
import com.brohit.plantdoctor.presentation.component.SnackImage
import com.brohit.plantdoctor.presentation.component.SnackItem
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private const val TAG = "DetectionScreen"

@Destination
@Composable
fun DetectionScreen(
    imageUri: Uri,
    navigator: DestinationsNavigator,
    vm: DetectionViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val state = vm.state.value
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.plant == null) {
                var offlinePrediction by remember {
                    mutableStateOf("")
                }
                Image(
                    modifier = Modifier.fillMaxSize(.7f),
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "cropped image"
                )
                if (offlinePrediction.isNotEmpty())
                    Text(
                        text = "Predicted Offline: $offlinePrediction",
                        modifier = Modifier.fillMaxWidth(.7f)
                    )
                Row {
                    PdButton(
                        onClick = {
                            vm.predict(imageUri)
                            val className = vm.predictOffline(
                                ImageDecoder.decodeBitmap(
                                    ImageDecoder.createSource(
                                        context.contentResolver,
                                        imageUri
                                    )
                                ), context
                            )
                            offlinePrediction = className
                        },
                        enabled = state.isLoading.not()
                    ) {
                        Text(text = "Detect")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    PdButton(
                        backgroundGradient = PlantDoctorTheme.colors.gradient2_3,
                        onClick = { navigator.popBackStack() },
                        enabled = state.isLoading.not()
                    ) {
                        Text(text = "Retake")
                    }
                }
            }
            if (state.error.isNotEmpty()) {
                SnackbarManager.showMessage(state.error)
            }
            if (state.plant != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(.8f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SnackImage(
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp),
                            imageUrl = imageUri, contentDescription = null
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "Predicted"
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SnackItem(plant = state.plant, onSnackClick = {})
                            Text(text = "Status : ${state.plant.status}")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "About ${state.plant.name}",
                        fontSize = 32.sp,
                        modifier = Modifier.fillMaxWidth(.9f)
                    )
                    Text(
                        text = state.plant.tagLine, modifier = Modifier.fillMaxWidth(.9f),
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    if (state.plant.status.contains(Regex("healthy"))) {
                        Text(
                            text = "Detected Plant is Healthy",
                            modifier = Modifier.fillMaxWidth(.9f)
                        )
                    } else {
                        Text(
                            text = "Plant has ${state.plant.status} disease!",
                            modifier = Modifier.fillMaxWidth(.9f),
                            fontSize = 24.sp
                        )
                    }


                }
                PdButton(
                    backgroundGradient = PlantDoctorTheme.colors.gradient2_3,
                    onClick = { navigator.popBackStack() },
                ) {
                    Text(text = "Retake")
                }
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