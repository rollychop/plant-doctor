package com.brohit.plantdoctor.data.remote.dto

data class PlantCollectionDTO(
    val id: Long,
    val name: String?,
    val plants: List<PlantDTO>?,
    val type: Int? = 0 //0 means normal non-zero Highlighted
)
