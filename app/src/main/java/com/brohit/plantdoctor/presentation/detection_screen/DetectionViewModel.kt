package com.brohit.plantdoctor.presentation.detection_screen

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brohit.plantdoctor.common.Resource
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val repo: PlantDetectionRepository
) : ViewModel() {
    private val _state = mutableStateOf(DetectionScreenState())
    val state: State<DetectionScreenState> = _state

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
                }
            }
        }.launchIn(viewModelScope)
    }

}