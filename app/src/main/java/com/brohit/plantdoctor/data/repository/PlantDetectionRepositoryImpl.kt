package com.brohit.plantdoctor.data.repository

import com.brohit.plantdoctor.data.remote.PlantDoctorApi
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import javax.inject.Inject

class PlantDetectionRepositoryImpl
@Inject constructor(private val api: PlantDoctorApi) :
    PlantDetectionRepository {
    override suspend fun getPlantList(): List<Plant> {
        return api.getPlantList().map { Plant(it.name, it.isHealthy, it.diseaseName, it.imageUrl) }
    }
}