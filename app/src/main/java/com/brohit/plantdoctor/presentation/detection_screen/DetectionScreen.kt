package com.brohit.plantdoctor.presentation.detection_screen

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.domain.model.SnackbarManager
import com.brohit.plantdoctor.presentation.component.*
import com.brohit.plantdoctor.presentation.plant_detail_screen.BrandText
import com.brohit.plantdoctor.presentation.plant_detail_screen.LongText
import com.brohit.plantdoctor.presentation.plant_detail_screen.LongTextBGColor
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
            modifier = Modifier
                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (state.plant == null) {
                var offlinePrediction by remember {
                    mutableStateOf("")
                }
                DestinationBar(title = "Preview")
                Image(
                    modifier = Modifier.fillMaxSize(.7f),
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "cropped image"
                )
                Text(
                    text = if (offlinePrediction.isNotEmpty()) "Predicted Offline: $offlinePrediction" else "",
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
                            vm.predictImageNet(
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
                        Text(text = getDetectButtonLabel(state))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    if (state.isLoading.not())
                        PdButton(
                            backgroundGradient = PlantDoctorTheme.colors.gradient2_3,
                            onClick = { navigator.popBackStack() },
                            enabled = state.isLoading.not()
                        ) {
                            Text(text = "Close")
                        }
                }
            }
            if (state.error.isNotEmpty()) {
                SnackbarManager.showMessage(state.error)
            }
            if (state.plant != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(8.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(.8f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PlantImage(
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
                                PlantItem(plant = state.plant, onPlantClick = {})
                                Text(
                                    text = "Status : ${state.plant.status.replaceFirstChar { it.uppercaseChar() }}",
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PdDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        BrandText(
                            text = "About ${state.plant.name}",
                            modifier = Modifier.fillMaxWidth()
                        )
                        LongText(text = state.plant.about)
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    if (state.plant.status.lowercase().contains(Regex("healthy"))) {
                        item {
                            Text(
                                text = "Detected Plant is Healthy",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        item {
                            BrandText(
                                text = "Plant has ${state.plant.status} disease!",
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        state.plantDetail?.let {
                            val first = it.diseases.firstOrNull { disease ->
                                disease.name.replace("_", " ").contains(
                                    state.plant.status.replace("_", " "),
                                    ignoreCase = true
                                )
                            } ?: return@let
                            item {
                                LongText(text = first.aboutDisease)
                                PdDivider()
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            if (first.solutions.isNotEmpty()) {
                                item {
                                    BrandText(text = "Treatment Steps")
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                            itemsIndexed(first.solutions) { idx, solText ->
                                PdCard(
                                    elevation = 2.dp,
                                    border = BorderStroke(
                                        1.dp,
                                        PlantDoctorTheme.colors.uiBorder,
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
                                        .clickable {
                                        }
                                ) {
                                    Text(
                                        text = "Step: ${idx + 1}",
                                        modifier = Modifier
                                            .padding(
                                                vertical = 4.dp,
                                                horizontal = 12.dp
                                            )
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                                LongTextBGColor(
                                    text = solText
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                    }
                    item {
                        Row {
                            PdButton(
                                backgroundGradient = PlantDoctorTheme.colors.gradient2_3,
                                onClick = { vm.clearResult() },
                            ) {
                                Text(
                                    text = "Back to Preview",
                                    color = PlantDoctorTheme.colors.textSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            PdButton(
                                backgroundGradient = PlantDoctorTheme.colors.gradient2_3,
                                onClick = { navigator.popBackStack() },
                            ) {
                                Text(text = "Close", color = PlantDoctorTheme.colors.textSecondary)
                            }
                        }
                    }


                }
                Spacer(modifier = Modifier.height(16.dp))
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

private fun getDetectButtonLabel(state: DetectionScreenState) = when {
    state.isLoading -> "Detecting"
    state.error.isNotEmpty() -> "Retry"
    else -> "Detect"
}