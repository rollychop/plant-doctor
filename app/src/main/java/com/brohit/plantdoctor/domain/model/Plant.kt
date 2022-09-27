package com.brohit.plantdoctor.domain.model

data class Plant(
    val name: String,
    val isHealthy: Boolean,
    val diseaseName: String?,
    val imageUrl: String?
)
