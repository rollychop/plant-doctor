package com.brohit.plantdoctor.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme

@Composable
fun PdCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = PlantDoctorTheme.colors.uiBackground,
    contentColor: Color = PlantDoctorTheme.colors.textPrimary,
    border: BorderStroke? = null,
    elevation: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    PdSurface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        elevation = elevation,
        border = border,
        content = content
    )
}

@Preview("default")
@Composable
private fun CardPreview() {
    PlantDoctorTheme {
        PdCard {
            Text(text = "Demo", modifier = Modifier.padding(16.dp))
        }
    }
}