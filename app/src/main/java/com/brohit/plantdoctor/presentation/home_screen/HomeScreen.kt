package com.brohit.plantdoctor.presentation.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph


@RootNavGraph(start = true)
@Destination()
@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {

            Text(text = "Home")
        }
    }

}