package com.brohit.plantdoctor.data.repository

import android.net.Uri
import android.util.Log
import com.brohit.plantdoctor.common.Resource
import com.brohit.plantdoctor.common.toModel
import com.brohit.plantdoctor.data.remote.PlantDoctorApi
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.domain.model.PlantCollection
import com.brohit.plantdoctor.domain.model.PlantRepoStatic
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject


private const val TAG = "PlantDetectionRepository"

class PlantDetectionRepositoryImpl
@Inject constructor(private val api: PlantDoctorApi) :
    PlantDetectionRepository {


    override suspend fun getPlantList(): List<Plant> {
        return api.getPlantList().map { it.toModel() }
    }


    override fun predict(imageUri: Uri): Flow<Resource<Plant>> = flow {
        emit(Resource.Loading())
        try {
            Log.d(TAG, "predict: $imageUri")
            val imgFile = File(imageUri.path!!)

            val reqFile: RequestBody = imgFile.asRequestBody()
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", imgFile.name, reqFile)

            val plantDTO = api.predict(
                body
            )
            emit(Resource.Success(plantDTO.toModel()))
        } catch (io: IOException) {
            emit(Resource.Error("IO Error : ${io.localizedMessage}"))
            Log.e(TAG, "predict: $io", io)
        } catch (http: HttpException) {
            emit(Resource.Error("Something went Wrong"))
            Log.e(TAG, "predict: ", http)
        } catch (i: IllegalArgumentException) {
            emit(Resource.Error("URI ERROR ${i.message}"))
            Log.e(TAG, "predict: ", i)
        } catch (np: NullPointerException) {
            emit(Resource.Error("URI ERROR ${np.message}"))
        }
    }

    override fun getPlantCollection(): Flow<Resource<List<PlantCollection>>> = flow {
        emit(Resource.Loading(PlantRepoStatic.getPlants()))
        try {
            val plantCollection = api.getPlantCollection().map { it.toModel() }
            emit(Resource.Success(plantCollection))
        } catch (io: IOException) {
            emit(Resource.Error("Internet Error", PlantRepoStatic.getPlants()))
            Log.e(TAG, "predict: ", io)
        } catch (http: HttpException) {
            emit(Resource.Error("Something went Wrong", PlantRepoStatic.getPlants()))
            Log.e(TAG, "predict: ", http)
        } catch (i: IllegalArgumentException) {
            emit(Resource.Error("URI ERROR ${i.message}", PlantRepoStatic.getPlants()))
            Log.e(TAG, "predict: ", i)
        }
    }
}