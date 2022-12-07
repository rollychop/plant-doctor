package com.brohit.plantdoctor.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.brohit.plantdoctor.presentation.component.PlantDoctorScaffold
import com.brohit.plantdoctor.presentation.component.PdSnackbar
import com.brohit.plantdoctor.presentation.home_screen.PdBottomBar
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i(TAG, "Permission granted")
        } else {
            Log.i(TAG, "Permission denied")
        }
    }

    private fun requestCameraPermission(showDialog: (() -> Unit)? = null) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i(TAG, "Permission previously granted")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                Log.i(TAG, "Show camera permissions dialog")
                showDialog?.invoke()
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appState = rememberPlantDoctorAppState()
            PlantDoctorTheme {
                PlantDoctorScaffold(
                    bottomBar = {
                        if (appState.shouldShowBottomBar)
                            PdBottomBar(
                                tabs = appState.bottomBarTabs,
                                currentRoute = appState.currentDestination.route,
                                navigateToRoute = {
                                    appState.navigateToBottomBarRoute(it.route)
                                }
                            )
                    },
                    snackbarHost = {
                        SnackbarHost(
                            hostState = it,
                            modifier = Modifier.systemBarsPadding(),
                            snackbar = { snackbarData -> PdSnackbar(snackbarData) }
                        )
                    },
                    scaffoldState = appState.scaffoldState,
                ) { innerPaddingModifier ->
                    Box(modifier = Modifier.padding(innerPaddingModifier)) {
                        DestinationsNavHost(
                            NavGraphs.root,
                            navController = appState.navController
                        )
                    }

                }
            }
        }
//        requestCameraPermission()
    }


}
