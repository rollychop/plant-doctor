package com.brohit.plantdoctor.data.remote

import com.brohit.plantdoctor.data.remote.dto.PlantDTO
import retrofit2.Response
import retrofit2.http.GET

interface PlantDoctorApi {
    @GET("/")
    fun getHome(): Response<Unit>

    @GET("/plant-list")
    suspend fun getPlantList(): List<PlantDTO>
}