package com.brohit.plantdoctor.domain.repository

import android.net.Uri
import com.brohit.plantdoctor.common.Resource
import com.brohit.plantdoctor.domain.model.Plant
import kotlinx.coroutines.flow.Flow

interface PlantDetectionRepository {
    suspend fun getPlantList(): List<Plant>
    fun predict(imageUri: Uri): Flow<Resource<Plant>>
}