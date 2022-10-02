package com.brohit.plantdoctor.common

import com.brohit.plantdoctor.data.remote.dto.PlantDTO
import com.brohit.plantdoctor.domain.model.Plant

fun Plant.toDTO() = PlantDTO(
    this.name, this.isHealthy, this.diseaseName, this.imageUrl
)

fun PlantDTO.toModel() = Plant(
    this.name, this.isHealthy, this.diseaseName, this.imageUrl
)