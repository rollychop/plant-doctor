package com.brohit.plantdoctor.data.remote.dto

data class PlantDetailDTO(
    val id: Long,
    val name: String,
    val disease: List<PlantDiseaseDTO>?,
    val plant: PlantDTO,
)