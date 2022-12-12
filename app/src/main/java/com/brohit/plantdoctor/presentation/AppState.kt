package com.brohit.plantdoctor.presentation

import android.content.res.Resources
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.brohit.plantdoctor.domain.model.SnackbarManager
import com.brohit.plantdoctor.presentation.destinations.Destination
import com.brohit.plantdoctor.presentation.home_screen.HomeSections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private const val TAG = "AppState"


/**
 * Remembers and creates an instance of [PdAppState]
 */
@Composable
fun rememberPdAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackBarManager, resources, coroutineScope) {
        Log.d(TAG, "rememberPlantDoctorAppState: $navController")
        PdAppState(scaffoldState, navController, snackBarManager, resources, coroutineScope)
    }


@Stable
class PdAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = message.msg
                    scaffoldState.snackbarHostState.showSnackbar(text)
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    val bottomBarTabs = HomeSections.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.destination.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val currentDestination: Destination
        @Composable get() = navController.appCurrentDestinationAsState().value
            ?: NavGraphs.root.startAppDestination

    private val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }


}


private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)


private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}


@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}