package com.brohit.plantdoctor.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brohit.plantdoctor.presentation.ui.theme.AlphaNearOpaque
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme

@Composable
fun DestinationBar(
    modifier: Modifier = Modifier, title: String = "Home",
    imageVector: ImageVector? = null
) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = PlantDoctorTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = PlantDoctorTheme.colors.textSecondary,
            elevation = 5.dp
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            imageVector?.let { _ ->
                IconButton(onClick = { }) {
                    Icon(imageVector = imageVector, contentDescription = title)
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                color = PlantDoctorTheme.colors.textSecondary,
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold
            )
        }
        PdDivider()
    }
}

@Preview("default", "rectangle")
@Composable
fun DestinationPreview() {
    PlantDoctorTheme {
        DestinationBar()
    }
}