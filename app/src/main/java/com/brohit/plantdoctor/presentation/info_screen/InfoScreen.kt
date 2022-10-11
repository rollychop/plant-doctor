package com.brohit.plantdoctor.presentation.info_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination()
@Composable
fun InfoScreen(navigator: DestinationsNavigator) {
    Box {
        Text(text = "Feed")
    }
}