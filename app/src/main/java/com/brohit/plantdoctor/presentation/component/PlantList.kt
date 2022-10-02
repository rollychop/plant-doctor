package com.brohit.plantdoctor.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.domain.model.Plant

@Composable
fun PlantList(
    modifier: Modifier = Modifier,
    plantList: List<Plant> = emptyList()
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(plantList) { plant ->
            PlantItem(plant = plant)
        }
    }
}

@Composable
fun PlantItem(modifier: Modifier = Modifier, plant: Plant) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = rememberImagePainter(data = plant.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .width(160.dp)
                .height(160.dp)
                .padding(2.dp)
                .clip(RoundedCornerShape(160.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            text = plant.name,
            textAlign = TextAlign.Center
        )
    }
}