package com.brohit.plantdoctor.presentation.detection_screen

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brohit.plantdoctor.common.Resource
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "DetectionViewModel"

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val repo: PlantDetectionRepository
) : ViewModel() {
    private val _state = mutableStateOf(DetectionScreenState())
    val state: State<DetectionScreenState> = _state

    fun clearResult() {
        _state.value = DetectionScreenState()
    }

    fun predictOffline(bitmap: Bitmap, context: Context): String {
        /*if (model == null)
            model = ModelUnquant.newInstance(context)
        val model = ModelUnquant.newInstance(context)
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val img =
            Bitmap.createScaledBitmap(bitmap, 224, 224, true)
                .copy(Bitmap.Config.ARGB_8888, false)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(img)
        inputFeature0.loadBuffer(tensorImage.buffer)
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        return Constants.CLASS_LIST[outputFeature0.floatArray.withIndex()
            .maxBy { it.value }.index]*/
        return "No Prediction"

    }

    fun predictImageNet(bitmap: Bitmap, context: Context) {
        val contextWrapper = context as ContextWrapper

    }

    private fun loadPlantDetails(plant: Plant) {
        repo.getPlantDetail(plant.id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(plantDetail = result.data)
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun predict(imageUri: Uri) {
        repo.predict(imageUri).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = DetectionScreenState(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value =
                        DetectionScreenState(error = result.message ?: "Unexpected Error")
                }
                is Resource.Success -> {
                    _state.value = DetectionScreenState(plant = result.data)
                    result.data?.let { loadPlantDetails(it) }
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
//        model?.close()
        super.onCleared()
    }

}