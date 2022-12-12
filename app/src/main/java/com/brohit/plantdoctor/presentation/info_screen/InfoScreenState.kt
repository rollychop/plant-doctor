package com.brohit.plantdoctor.presentation.info_screen

data class InfoScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val markdownText: String? = null
)
