//
//
package com.brohit.plantdoctor.presentation.home_screen
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.selection.selectable
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.ContentAlpha
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.LocalContentAlpha
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Slider
//import androidx.compose.material.SliderDefaults
//import androidx.compose.material.Text
//import androidx.compose.material.TopAppBar
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Done
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import com.brohit.plantdoctor.domain.model.Filter
//import com.brohit.plantdoctor.presentation.component.FilterChip
//import com.brohit.plantdoctor.presentation.component.PlantDoctorScaffold
//import com.brohit.plantdoctor.presentation.ui.theme.PlantDoctorTheme
//import com.brohit.plantdoctor.domain.model.PlantRepoStatic
//import com.google.accompanist.flowlayout.FlowMainAxisAlignment
//import com.google.accompanist.flowlayout.FlowRow
//
//@Composable
//fun FilterScreen(
//    onDismiss: () -> Unit
//) {
////    var sortState by remember { mutableStateOf(PlantRepoStatic.getSortDefault()) }
//    var maxCalories by remember { mutableStateOf(0f) }
////    val defaultFilter = PlantRepoStatic.getSortDefault()
//
//    Dialog(onDismissRequest = onDismiss) {
//
//        val priceFilters = remember { PlantRepoStatic.getPriceFilters() }
//        val categoryFilters = remember { PlantRepoStatic.getCategoryFilters() }
//        val lifeStyleFilters = remember { PlantRepoStatic.getLifeStyleFilters() }
//        PlantDoctorScaffold(
//            topBar = {
//                TopAppBar(
//                    navigationIcon = {
//                        IconButton(onClick = onDismiss) {
//                            Icon(
//                                imageVector = Icons.Filled.Close,
//                                contentDescription = "close"
//                            )
//                        }
//                    },
//                    title = {
//                        Text(
//                            text = "filter",
//                            modifier = Modifier.fillMaxWidth(),
//                            textAlign = TextAlign.Center,
//                            style = MaterialTheme.typography.h6
//                        )
//                    },
//                    actions = {
//                        var resetEnabled = sortState != defaultFilter
//                        IconButton(
//                            onClick = { /* TODO: Open search */ },
//                            enabled = resetEnabled
//                        ) {
//                            val alpha = if (resetEnabled) {
//                                ContentAlpha.high
//                            } else {
//                                ContentAlpha.disabled
//                            }
//                            CompositionLocalProvider(LocalContentAlpha provides alpha) {
//                                Text(
//                                    text = "reset",
//                                    style = MaterialTheme.typography.body2
//                                )
//                            }
//                        }
//                    },
//                    backgroundColor = PlantDoctorTheme.colors.uiBackground
//                )
//            }
//        ) {
//            Column(
//                Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(horizontal = 24.dp, vertical = 16.dp),
//            ) {
//                SortFiltersSection(
//                    sortState = sortState,
//                    onFilterChange = { filter ->
//                        sortState = filter.name
//                    }
//                )
//                FilterChipSection(
//                    title = "price",
//                    filters = priceFilters
//                )
//                FilterChipSection(
//                    title = "category",
//                    filters = categoryFilters
//                )
//
//                MaxCalories(
//                    sliderPosition = maxCalories,
//                    onValueChanged = { newValue ->
//                        maxCalories = newValue
//                    }
//                )
//                FilterChipSection(
//                    title = "lifestyle",
//                    filters = lifeStyleFilters
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun FilterChipSection(title: String, filters: List<Filter>) {
//    FilterTitle(text = title)
//    FlowRow(
//        mainAxisAlignment = FlowMainAxisAlignment.Center,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 12.dp, bottom = 16.dp)
//            .padding(horizontal = 4.dp)
//    ) {
//        filters.forEach { filter ->
//            FilterChip(
//                filter = filter,
//                modifier = Modifier.padding(end = 4.dp, bottom = 8.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun SortFiltersSection(sortState: String, onFilterChange: (Filter) -> Unit) {
//    FilterTitle(text = "sort")
//    Column(Modifier.padding(bottom = 24.dp)) {
//        SortFilters(
//            sortState = sortState,
//            onChanged = onFilterChange
//        )
//    }
//}
//
//@Composable
//fun SortFilters(
//    sortFilters: List<Filter>,// = PlantRepoStatic.getSortFilters(),
//    sortState: String,
//    onChanged: (Filter) -> Unit
//) {
//
//    sortFilters.forEach { filter ->
//        SortOption(
//            text = filter.name,
//            icon = filter.icon,
//            selected = sortState == filter.name,
//            onClickOption = {
//                onChanged(filter)
//            }
//        )
//    }
//}
//
//@Composable
//fun MaxCalories(sliderPosition: Float, onValueChanged: (Float) -> Unit) {
//    FlowRow {
//        FilterTitle(text = "max calories")
//        Text(
//            text = "per serving",
//            style = MaterialTheme.typography.body2,
//            color = PlantDoctorTheme.colors.brand,
//            modifier = Modifier.padding(top = 5.dp, start = 10.dp)
//        )
//    }
//    Slider(
//        value = sliderPosition,
//        onValueChange = { newValue ->
//            onValueChanged(newValue)
//        },
//        valueRange = 0f..300f,
//        steps = 5,
//        modifier = Modifier
//            .fillMaxWidth(),
//        colors = SliderDefaults.colors(
//            thumbColor = PlantDoctorTheme.colors.brand,
//            activeTrackColor = PlantDoctorTheme.colors.brand
//        )
//    )
//}
//
//@Composable
//fun FilterTitle(text: String) {
//    Text(
//        text = text,
//        style = MaterialTheme.typography.h6,
//        color = PlantDoctorTheme.colors.brand,
//        modifier = Modifier.padding(bottom = 8.dp)
//    )
//}
//@Composable
//fun SortOption(
//    text: String,
//    icon: ImageVector?,
//    onClickOption: () -> Unit,
//    selected: Boolean
//) {
//    Row(
//        modifier = Modifier
//            .padding(top = 14.dp)
//            .selectable(selected) { onClickOption() }
//    ) {
//        if (icon != null) {
//            Icon(imageVector = icon, contentDescription = null)
//        }
//        Text(
//            text = text,
//            style = MaterialTheme.typography.subtitle1,
//            modifier = Modifier
//                .padding(start = 10.dp)
//                .weight(1f)
//        )
//        if (selected) {
//            Icon(
//                imageVector = Icons.Filled.Done,
//                contentDescription = null,
//                tint = PlantDoctorTheme.colors.brand
//            )
//        }
//    }
//}
//@Preview("filter screen")
//@Composable
//fun FilterScreenPreview() {
//    FilterScreen(onDismiss = {})
//}