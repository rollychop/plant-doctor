package com.brohit.plantdoctor.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.brohit.plantdoctor.domain.model.PlantDisease
import com.brohit.plantdoctor.domain.model.fakeDisease
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme

@Composable
fun DiseaseCollection(
    modifier: Modifier = Modifier,
    name: String,
    diseases: List<PlantDisease>,
    active: PlantDisease? = null,
    onDiseaseClick: (PlantDisease) -> Unit
) {

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                color = PlantDoctorTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
        }
        LazyRow(
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(diseases) { disease ->
                PdSurface(
                    color = if (active != null && active == disease)
                        PlantDoctorTheme.colors.uiBorder else PlantDoctorTheme.colors.uiBackground,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(onClick = { onDiseaseClick(disease) })
                            .padding(8.dp)
                    ) {
                        PdSurface(
                            color = Color.LightGray,
                            elevation = 2.dp,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.size(120.dp)
                        ) {
                            Image(
                                painter = rememberImagePainter(data = disease.imgUrl),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Text(
                            text = disease.name.replace('_', ' '),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.subtitle1,
                            color = PlantDoctorTheme.colors.textSecondary,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .width(100.dp)
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun DiseasePreview() {
    PlantDoctorTheme(darkTheme = true) {
        DiseaseCollection(
            name = "Known Disease",
            diseases = fakeDisease,
            onDiseaseClick = {})
    }
}