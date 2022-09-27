package com.brohit.plantdoctor.data.remote.dto

data class PlantDTO(
    val name: String,
    val isHealthy: Boolean,
    val diseaseName: String?,
    val imageUrl: String?
)
