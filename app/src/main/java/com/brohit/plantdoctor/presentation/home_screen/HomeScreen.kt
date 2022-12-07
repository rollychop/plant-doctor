package com.brohit.plantdoctor.presentation.home_screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brohit.plantdoctor.domain.model.PlantCollection
import com.brohit.plantdoctor.presentation.component.PdDivider
import com.brohit.plantdoctor.presentation.component.PdSurface
import com.brohit.plantdoctor.presentation.component.SnackCollection
import com.brohit.plantdoctor.presentation.ui.theme.AlphaNearOpaque
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private const val TAG = "HomeScreen"

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    PdSurface(modifier = Modifier.fillMaxSize()) {
        Feed(plantCollections = state.plantCollection) {

        }
    }

}

@Composable
fun Feed(
    modifier: Modifier = Modifier,
    plantCollections: List<PlantCollection>,
    onSnackClick: (Long) -> Unit
) {

//    val filters = remember { SnackRepo.getFilters() }
    Feed(
        plantCollections,
//        filters,
        onSnackClick,
        modifier
    )
}

@Composable
private fun Feed(
    plantCollections: List<PlantCollection>,
//    filters: List<Filter>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    PdSurface(modifier = modifier.fillMaxSize()) {
        Box {
            SnackCollectionList(
                plantCollections = plantCollections,
//                filters = filters,
                onSnackClick = onSnackClick
            )
            DestinationBar()
        }
    }
}

@Composable
private fun SnackCollectionList(
    modifier: Modifier = Modifier,
    plantCollections: List<PlantCollection>,/*
    filters: List<Filter> = emptyList(),*/
    onSnackClick: (Long) -> Unit
) {
    var filtersVisible by rememberSaveable {
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
//                FilterBar(filters, onShowFilters = { filtersVisible = true })
            }
            itemsIndexed(plantCollections) { index, snackCollection ->
                if (index > 0) {
                    PdDivider(thickness = 2.dp)
                }

                SnackCollection(
                    plantCollection = snackCollection,
                    onSnackClick = onSnackClick,
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
//        FilterScreen(
//            onDismiss = { filtersVisible = false }
//        )
    }
}


@Composable
fun DestinationBar(modifier: Modifier = Modifier, title: String = "Home") {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = PlantDoctorTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = PlantDoctorTheme.colors.textSecondary,
            elevation = 5.dp
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                color = PlantDoctorTheme.colors.textSecondary,
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            /* IconButton(
                 onClick = {  },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    tint = PlantDoctorTheme.colors.brand,
                    contentDescription = null
                )
            }*/
        }
        PdDivider()
    }
}
