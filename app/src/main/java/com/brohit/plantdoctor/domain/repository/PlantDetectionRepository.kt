package com.brohit.plantdoctor.domain.repository

import com.brohit.plantdoctor.domain.model.Plant

interface PlantDetectionRepository {
    suspend fun getPlantList(): List<Plant>
}