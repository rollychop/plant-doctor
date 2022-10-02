package com.brohit.plantdoctor.presentation.detection_screen

import com.brohit.plantdoctor.domain.model.Plant

data class DetectionScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val plant: Plant? = null
)
