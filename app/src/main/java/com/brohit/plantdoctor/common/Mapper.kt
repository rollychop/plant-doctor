package com.brohit.plantdoctor.common

import com.brohit.plantdoctor.data.remote.dto.PlantCollectionDTO
import com.brohit.plantdoctor.data.remote.dto.PlantDTO
import com.brohit.plantdoctor.data.remote.dto.PlantDetailDTO
import com.brohit.plantdoctor.domain.model.*
import java.util.Locale


fun PlantDTO.toModel() = Plant(
    name ?: "",
    imageUrl ?: Constants.IMG_URL,
    isHealthy,
    status ?: "",
    tagLine ?: "",
    tags ?: emptySet(),
    id,
    about ?: "",

    )

fun PlantCollectionDTO.toModel() = PlantCollection(
    id,
    name ?: "Plants",
    plants?.map { it.toModel() } ?: emptyList(),
    if (type == null || type == 0) CollectionType.Normal else CollectionType.Highlight
)

fun PlantDetailDTO.toModel() = PlantDetail(
    name = name,
    plant = plant.toModel(),
    diseases = disease?.map { disease ->
        PlantDisease(
            disease.name,
            disease.imageUrl ?: Constants.DISEASE_IMG_URL,
            disease.about ?: "No Detail Available",
            disease.solutions ?: listOf("Pest 1", "Pest 2")
        )
    } ?: emptyList()
)

val IN: Locale
    get() = Locale("en", "IN")