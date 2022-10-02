package com.brohit.plantdoctor.data.repository

import android.net.Uri
import android.util.Log
import com.brohit.plantdoctor.common.Resource
import com.brohit.plantdoctor.common.toModel
import com.brohit.plantdoctor.data.remote.PlantDoctorApi
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

private const val TAG = "PlantDetectionRepositor"

class PlantDetectionRepositoryImpl
@Inject constructor(private val api: PlantDoctorApi) :
    PlantDetectionRepository {
    override suspend fun getPlantList(): List<Plant> {
        return api.getPlantList().map { it.toModel() }
    }

    override fun predict(imageUri: Uri): Flow<Resource<Plant>> = flow {
        emit(Resource.Loading())
        try {
            val imgFile = File(imageUri.toString())
            val plantDTO = api.predict(
                MultipartBody.Part.createFormData(
                    "image",
                    imgFile.name,
                    imgFile.asRequestBody()
                )
            )
            emit(Resource.Success(plantDTO.toModel()))
        } catch (io: IOException) {
            emit(Resource.Error("Internet Error"))
            Log.e(TAG, "predict: ", io)
        } catch (http: HttpException) {
            emit(Resource.Error("Something went Wrong"))
            Log.e(TAG, "predict: ", http)
        } catch (i: IllegalArgumentException) {
            emit(Resource.Error("URI ERROR ${i.message}"))
            Log.e(TAG, "predict: ", i)
        }
    }
}