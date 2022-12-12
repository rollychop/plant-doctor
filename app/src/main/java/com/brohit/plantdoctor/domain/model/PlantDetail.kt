package com.brohit.plantdoctor.domain.model

data class PlantDetail(
    val name: String,
    val plant: Plant,
    val diseases: List<PlantDisease>,
)
