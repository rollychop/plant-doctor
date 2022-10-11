package com.brohit.plantdoctor.presentation.home_screen

import androidx.compose.runtime.Immutable
import com.brohit.plantdoctor.domain.model.PlantCollection

@Immutable
data class HomeScreenState(
    val plantCollection: List<PlantCollection> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
) {
}