package com.brohit.plantdoctor.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.domain.model.CollectionType
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.domain.model.PlantCollection
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme


private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

// The Cards show a gradient which spans 3 cards and scrolls with parallax.
private val gradientWidth
    @Composable
    get() = with(LocalDensity.current) {
        (3 * (HighlightCardWidth + HighlightCardPadding).toPx())
    }

@Composable
fun PlantCollection(
    plantCollection: PlantCollection,
    onPlantClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0,
    highlight: Boolean = true
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = plantCollection.name,
                style = MaterialTheme.typography.h6,
                color = PlantDoctorTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
        }
        if (highlight && plantCollection.type == CollectionType.Highlight) {
            HighlightedSnacks(index, plantCollection.plantList, onPlantClick)
        } else {
            Plants(plantCollection.plantList, onPlantClick)
        }
    }
}

@Composable
private fun HighlightedSnacks(
    index: Int,
    plants: List<Plant>,
    onSnackClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    val gradient = when ((index / 2) % 2) {
        0 -> PlantDoctorTheme.colors.gradient6_1
        else -> PlantDoctorTheme.colors.gradient6_2
    }
    // The Cards show a gradient which spans 3 cards and scrolls with parallax.
    val gradientWidth = with(LocalDensity.current) {
        (6 * (HighlightCardWidth + HighlightCardPadding).toPx())
    }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        itemsIndexed(plants) { index, plant ->
            HighlightPlantItem(
                plant,
                onSnackClick,
                index,
                gradient,
                gradientWidth,
                scroll.value
            )
        }
    }
}

@Composable
private fun Plants(
    plants: List<Plant>,
    onPlantClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ) {
        items(plants) { plant ->
            PlantItem(plant, onPlantClick)
        }
    }
}

@Composable
fun PlantItem(
    plant: Plant,
    onPlantClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    PdSurface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = { onPlantClick(plant.id) })
                .padding(8.dp)
        ) {
            PlantImage(
                imageUrl = plant.imageUrl,
                elevation = 4.dp,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = plant.name,
                style = MaterialTheme.typography.subtitle1,
                color = PlantDoctorTheme.colors.textSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun HighlightPlantItem(
    plant: Plant,
    onPlantClick: (Long) -> Unit,
    index: Int,
    gradient: List<Color>,
    gradientWidth: Float,
    scroll: Int,
    modifier: Modifier = Modifier
) {
    val left = index * with(LocalDensity.current) {
        (HighlightCardWidth + HighlightCardPadding).toPx()
    }
    PdCard(
        modifier = modifier
            .size(
                width = 170.dp,
                height = 250.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = { onPlantClick(plant.id) })
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {
                val gradientOffset = left - (scroll / 3f)
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .offsetGradientBackground(gradient, gradientWidth, gradientOffset)
                )
                PlantImage(
                    imageUrl = plant.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plant.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6,
                color = PlantDoctorTheme.colors.textSecondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = plant.tagLine,
                style = MaterialTheme.typography.body1,
                color = PlantDoctorTheme.colors.textHelp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun PlantImage(
    imageUrl: Any,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    PdSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = CircleShape,
        modifier = modifier
    ) {
        /* AsyncImage(
             model = ImageRequest.Builder(LocalContext.current)
                 .data(imageUrl)
                 .crossfade(true)
                 .build(),
             contentDescription = contentDescription,*//*
            placeholder = painterResource(R.drawable.placeholder),*//*
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )*/
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

