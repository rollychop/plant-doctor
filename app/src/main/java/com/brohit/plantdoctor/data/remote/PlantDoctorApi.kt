package com.brohit.plantdoctor.data.remote

import com.brohit.plantdoctor.data.remote.dto.PlantDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PlantDoctorApi {
    @GET()
    fun getHome(): Response<Unit>

    @GET("plant-list")
    suspend fun getPlantList(): List<PlantDTO>

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part file: MultipartBody.Part
    ): PlantDTO
}