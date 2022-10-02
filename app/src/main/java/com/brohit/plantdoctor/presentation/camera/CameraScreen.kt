package com.brohit.plantdoctor.presentation.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: CameraScreenViewModel = hiltViewModel()
) {

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        ImageSelectorAndCropper()
    }

}