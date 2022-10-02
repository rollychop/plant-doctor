package com.brohit.plantdoctor.presentation.home_screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Camera
import androidx.compose.material.icons.sharp.Comment
import androidx.compose.material.icons.sharp.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.presentation.component.PlantList
import com.brohit.plantdoctor.presentation.destinations.DetectionScreenDestination
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private const val TAG = "HomeScreen"

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    var imageUri by remember {
        mutableStateOf(
            Uri.EMPTY
        )
    }

    if (imageUri != Uri.EMPTY) {
        navigator.navigate(DetectionScreenDestination(imageUri).route)
    }


    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the cropped image
            imageUri = result.uriContent
            Log.d(TAG, "HomeScreen: $imageUri")
        } else {
            // an error occurred cropping
            val exception = result.error
        }
    }
    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "Home")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    imageCropLauncher.launch(
                        options {
                            setAspectRatio(1, 1)
                            setGuidelines(CropImageView.Guidelines.ON)
                            setActivityTitle("Crop Image")
                            setAllowFlipping(false)
                            setRequestedSize(256, 256)
                        }
                    )
                },
                backgroundColor = MaterialTheme.colors.primarySurface
            ) {
                Icon(imageVector = Icons.Sharp.Camera, contentDescription = "Camera")
            }
        },
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar(
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                )

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Sharp.Home, contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Sharp.Camera, contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Sharp.Comment, contentDescription = null)
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
                .padding(horizontal = 8.dp, vertical = 16.dp)
//                .background(Color.Red) //debug
        ) {
            Text(text = "Plant List")
            PlantList(
                plantList = listOf(
                    Plant("Dummy"),
                    Plant("Dummy"),
                    Plant("Dummy"),
                    Plant("Dummy"),
                )
            )

        }
    }

}