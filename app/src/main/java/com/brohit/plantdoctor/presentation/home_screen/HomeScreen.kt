package com.brohit.plantdoctor.presentation.home_screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brohit.plantdoctor.domain.model.PlantCollection
import com.brohit.plantdoctor.domain.model.SnackbarManager
import com.brohit.plantdoctor.presentation.component.DestinationBar
import com.brohit.plantdoctor.presentation.component.PdDivider
import com.brohit.plantdoctor.presentation.component.PdSurface
import com.brohit.plantdoctor.presentation.component.PlantCollection
import com.brohit.plantdoctor.presentation.destinations.PlantDetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private const val TAG = "HomeScreen"


@Destination
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val state = viewModel.state.value
    PdSurface(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading)
            LinearProgressIndicator()
        Feed(plantCollections = state.plantCollection) {
            if ((state.isLoading || state.error.isNotEmpty()).not())
                navigator.navigate(PlantDetailScreenDestination(it).route)
            else {
                SnackbarManager.showMessage("Please Configure URL in INFO Tab and Restart app")
            }
        }
    }

}

@Composable
fun Feed(
    modifier: Modifier = Modifier,
    plantCollections: List<PlantCollection>,
    onPlantClick: (Long) -> Unit
) {

    Feed(
        plantCollections,
        onPlantClick,
        modifier
    )
}

@Composable
private fun Feed(
    plantCollections: List<PlantCollection>,
    onPlantClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    PdSurface(modifier = modifier.fillMaxSize()) {
        Box {
            SnackCollectionList(
                plantCollections = plantCollections,
                onPlantClick = onPlantClick
            )
            DestinationBar()
        }
    }
}

@Composable
private fun SnackCollectionList(
    modifier: Modifier = Modifier,
    plantCollections: List<PlantCollection>,
    onPlantClick: (Long) -> Unit
) {
    val filtersVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier) {
        LazyColumn {
            item {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                    )
                )
            }
            itemsIndexed(plantCollections) { index, plantCollection ->
                if (index > 0) {
                    PdDivider(thickness = 2.dp)
                }

                PlantCollection(
                    plantCollection = plantCollection,
                    onPlantClick = onPlantClick,
                    index = index
                )
            }
        }
    }
    AnimatedVisibility(
        visible = filtersVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {

    }
}



