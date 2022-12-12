package com.brohit.plantdoctor.presentation.plant_detail_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brohit.plantdoctor.domain.model.PlantDisease
import com.brohit.plantdoctor.presentation.component.*
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Destination(
    navArgsDelegate = PlantDetailNavArgs::class
)
@Composable
fun PlantDetailScreen(
    vm: PlantDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val state = vm.state.value
    PdSurface {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            DestinationBar(title = "Plant Detail")
            if (state.isLoading || state.error.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading)
                        CircularProgressIndicator()
                    else
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Unable to Load Plant!",
                                color = PlantDoctorTheme.colors.error
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = state.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            PdButton(onClick = { navigator.popBackStack() }) {
                                Text(text = "Back to Feed")
                            }
                        }
                }
                return@Column
            }
            val scrollState = rememberScrollState()
            val composableScope = rememberCoroutineScope()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                PlantItem(
                    plant = state.plantDetail.plant,
                    onPlantClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                BrandText(text = "About ${state.plantDetail.name}")
                LongText(text = state.plantDetail.plant.about)
                Spacer(modifier = Modifier.height(8.dp))
                PdDivider()
                Spacer(modifier = Modifier.height(8.dp))
                var selectedPlantDisease by remember {
                    mutableStateOf<PlantDisease?>(null)
                }
                DiseaseCollection(
                    name = if (state.plantDetail.diseases.isNotEmpty())
                        "Known Disease" else "Currently No Disease Available :(",
                    diseases = state.plantDetail.diseases,
                    active = selectedPlantDisease
                ) {
                    if (selectedPlantDisease == null) {
                        composableScope.launch {
                            val value = scrollState.value
                            delay(200)
                            scrollState.scrollTo(value + 1000)
                        }
                    }
                    selectedPlantDisease = if (selectedPlantDisease == it) null else it
                }

                selectedPlantDisease?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    BrandText(text = it.name.replace("_", " "))
                    Spacer(modifier = Modifier.height(8.dp))
                    LongText(text = it.aboutDisease)

                    if (it.solutions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        PdDivider()
                        Spacer(modifier = Modifier.height(4.dp))
                        SolutionBar(solutions = it.solutions)
                    }
                }
            }
        }
    }
}

@Composable
fun BrandText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
        color = PlantDoctorTheme.colors.brand,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}

@Composable
fun SolutionBar(text: String = "Solution", solutions: List<String> = emptyList()) {
    BrandText(text = text)
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(solutions) { sol ->
            PdCard(
                elevation = 2.dp,
                border = BorderStroke(
                    1.dp,
                    PlantDoctorTheme.colors.uiBorder,
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                    }
            ) {
                Text(
                    text = sol,
                    modifier = Modifier.padding(
                        vertical = 4.dp,
                        horizontal = 12.dp
                    )
                )
            }
        }
    }
}

@Composable
fun LongText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun LongTextBGColor(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(
                PlantDoctorTheme.colors.uiBorder,
                MaterialTheme.shapes.medium
            )
            .padding(
                vertical = 4.dp,
                horizontal = 12.dp
            ),
        textAlign = TextAlign.Justify
    )
}


@Preview(widthDp = 480, heightDp = 700)
@Composable
fun DetailScreenPreview() {
    PlantDoctorTheme(darkTheme = true) {
        BrandText(text = "About")
    }
}


