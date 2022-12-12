package com.brohit.plantdoctor.data.remote.dto

data class PlantDiseaseDTO(
    val name: String,
    val about: String?,
    val imageUrl: String?,
    val solutions: List<String>?,
)