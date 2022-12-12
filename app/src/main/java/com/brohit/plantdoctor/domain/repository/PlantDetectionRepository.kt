package com.brohit.plantdoctor.domain.repository

import android.net.Uri
import com.brohit.plantdoctor.common.Resource
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.domain.model.PlantCollection
import com.brohit.plantdoctor.domain.model.PlantDetail
import kotlinx.coroutines.flow.Flow

interface PlantDetectionRepository {
    suspend fun getPlantList(): List<Plant>
    fun predict(imageUri: Uri): Flow<Resource<Plant>>
    fun getPlantCollection(): Flow<Resource<List<PlantCollection>>>
    fun getPlantDetail(id: Long): Flow<Resource<PlantDetail>>
    fun getMarkdown(): Flow<Resource<String>>
}