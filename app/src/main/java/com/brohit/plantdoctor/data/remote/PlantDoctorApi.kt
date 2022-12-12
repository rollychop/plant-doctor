package com.brohit.plantdoctor.data.remote

import com.brohit.plantdoctor.data.remote.dto.PlantCollectionDTO
import com.brohit.plantdoctor.data.remote.dto.PlantDTO
import com.brohit.plantdoctor.data.remote.dto.PlantDetailDTO
import okhttp3.MultipartBody
import retrofit2.http.*

interface PlantDoctorApi {
    @GET("/get-collection")
    suspend fun getPlantCollection(): List<PlantCollectionDTO>

    @GET("/plant-detail")
    suspend fun getPlantDetail(@Query("_id") id: Long): PlantDetailDTO

    @GET("plant-list")
    suspend fun getPlantList(): List<PlantDTO>

    @GET("info")
    suspend fun getInfo(): String

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part() file: MultipartBody.Part
    ): PlantDTO
}