package com.brohit.plantdoctor.presentation.plant_detail_screen

import com.brohit.plantdoctor.domain.model.PlantDetail
import com.brohit.plantdoctor.domain.model.plants

data class PlantDetailState(
    val plantDetail: PlantDetail =
        PlantDetail(plants.first().name, plants.first(), emptyList()),
    val isLoading: Boolean = false,
    val error: String = ""
)