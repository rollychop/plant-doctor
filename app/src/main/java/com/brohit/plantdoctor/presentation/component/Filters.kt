package com.brohit.plantdoctor.presentation.component

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brohit.plantdoctor.domain.model.Filter
import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme

@Composable
fun FilterBar(
    filters: List<Filter>,
    onShowFilters: () -> Unit
) {

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 8.dp),
        modifier = Modifier.heightIn(min = 56.dp)
    ) {
        item {
            IconButton(onClick = onShowFilters) {
                Icon(
                    imageVector = Icons.Rounded.FilterList,
                    tint = PlantDoctorTheme.colors.brand,
                    contentDescription = "stringResource(R.string.label_filters)",
                    modifier = Modifier.diagonalGradientBorder(
                        colors = PlantDoctorTheme.colors.interactiveSecondary,
                        shape = CircleShape
                    )
                )
            }
        }
        items(filters) { filter ->
            FilterChip(filter = filter, shape = MaterialTheme.shapes.small)
        }
    }
}

@Composable
fun FilterChip(
    filter: Filter,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    val (selected, setSelected) = filter.enabled
    val backgroundColor by animateColorAsState(
        if (selected) PlantDoctorTheme.colors.brandSecondary else PlantDoctorTheme.colors.uiBackground
    )
    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = !selected,
        colors = PlantDoctorTheme.colors.interactiveSecondary,
        shape = shape
    )
    val textColor by animateColorAsState(
        if (selected) Color.Black else PlantDoctorTheme.colors.textSecondary
    )

    PdSurface(
        modifier = modifier.height(28.dp),
        color = backgroundColor,
        contentColor = textColor,
        shape = shape,
        elevation = 2.dp
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed) {
                Modifier.offsetGradientBackground(
                    PlantDoctorTheme.colors.interactiveSecondary,
                    200f,
                    0f
                )
            } else {
                Modifier.background(Color.Transparent)
            }
        Box(
            modifier = Modifier
                .toggleable(
                    value = selected,
                    onValueChange = setSelected,
                    interactionSource = interactionSource,
                    indication = null
                )
                .then(backgroundPressed)
                .then(border),
        ) {
            Text(
                text = filter.name,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                )
            )
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun FilterDisabledPreview() {
    PlantDoctorTheme {
        FilterChip(Filter(name = "Demo", enabled = false), Modifier.padding(4.dp))
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun FilterEnabledPreview() {
    PlantDoctorTheme {
        FilterChip(Filter(name = "Demo", enabled = true))
    }
}