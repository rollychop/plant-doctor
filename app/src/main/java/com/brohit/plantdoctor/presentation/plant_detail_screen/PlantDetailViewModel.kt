package com.brohit.plantdoctor.presentation.plant_detail_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brohit.plantdoctor.common.Resource
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import com.brohit.plantdoctor.presentation.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "PlantDetailViewModel"

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val plantDetectionRepository: PlantDetectionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(PlantDetailState())
    val state: State<PlantDetailState> = _state

    init {
        val navArgs: PlantDetailNavArgs = savedStateHandle.navArgs()
        getPlantDetail(navArgs.id)
    }

    private fun getPlantDetail(id: Long) {
        plantDetectionRepository.getPlantDetail(id).onEach {
            when (it) {
                is Resource.Loading -> {
                    _state.value = PlantDetailState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = PlantDetailState(plantDetail = it.data!!)
                }
                is Resource.Error -> {
                    _state.value = PlantDetailState(error = it.message ?: "Unexpected Error")

                }
            }

        }.launchIn(viewModelScope)
    }
}

data class PlantDetailNavArgs(val id: Long)