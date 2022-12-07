package com.brohit.plantdoctor.presentation.home_screen

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
class HomeScreenViewModel @Inject constructor(
    private val repo: PlantDetectionRepository
) : ViewModel() {
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    init {
        fetchPlantCollection()
    }

    private fun fetchPlantCollection() {
        repo.getPlantCollection().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = HomeScreenState(
                        isLoading = true,
                        plantCollection = result.data ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _state.value = HomeScreenState(
                        error = result.message ?: "Unexpected Error",
                        plantCollection = result.data ?: emptyList()
                    )
                }
                is Resource.Success -> {
                    _state.value = HomeScreenState(plantCollection = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }
}