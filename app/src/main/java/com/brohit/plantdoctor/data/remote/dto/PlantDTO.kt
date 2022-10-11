package com.brohit.plantdoctor.data.remote.dto

data class PlantDTO(
    val id: Long,
    val name: String?,
    val isHealthy: Boolean,
    val diseaseName: String?,
    val imageUrl: String?,
    val tagLine: String?,
    val tags: Set<String>?
)
