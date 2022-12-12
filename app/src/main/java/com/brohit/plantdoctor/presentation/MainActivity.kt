package com.brohit.plantdoctor.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.brohit.plantdoctor.BaseUrlDataStore
import com.brohit.plantdoctor.common.Configs
import com.brohit.plantdoctor.presentation.component.PdScaffold
import com.brohit.plantdoctor.presentation.component.PdSnackbar
import com.brohit.plantdoctor.presentation.home_screen.PdBottomBar
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val baseurl = BaseUrlDataStore(this).getBaseUrl.collectAsState(initial = "")
            baseurl.value?.let {
                Configs.mutableURL = it
            }
            val appState = rememberPdAppState()
            PlantDoctorTheme {
                PdScaffold(
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
                    snackBarHost = {
                        SnackbarHost(
                            hostState = it,
                            modifier = Modifier.systemBarsPadding(),
                            snackbar = { snackBarData -> PdSnackbar(snackBarData) }
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
    }


}
