package com.brohit.plantdoctor.presentation.plant_detail_screen

import com.brohit.plantdoctor.domain.model.PlantDetail

data class PlantDetailState(
    val plantDetail: PlantDetail =
        PlantDetail("Dummy", "Dummy"),
    val isLoading: Boolean = false,
    val error: String = ""
)