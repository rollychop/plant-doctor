package com.brohit.plantdoctor.presentation.plant_detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    private val plantDetectionRepository: PlantDetectionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


}