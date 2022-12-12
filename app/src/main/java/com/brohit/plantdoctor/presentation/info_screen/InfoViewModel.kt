package com.brohit.plantdoctor.presentation.info_screen

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
class InfoViewModel @Inject constructor(
    private val repo: PlantDetectionRepository
) : ViewModel() {
    private val _state = mutableStateOf(InfoScreenState())
    val state: State<InfoScreenState> = _state

    init {
        loadMarkdown()
    }

    private fun loadMarkdown() {
        repo.getMarkdown().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = InfoScreenState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = InfoScreenState(markdownText = result.data)
                }
                is Resource.Error -> {
                    _state.value = InfoScreenState(
                        error = result.message ?: "Unexpected Error!"
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

}