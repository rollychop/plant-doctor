package com.brohit.plantdoctor.common

import com.brohit.plantdoctor.data.remote.dto.PlantCollectionDTO
import com.brohit.plantdoctor.data.remote.dto.PlantDTO
import com.brohit.plantdoctor.domain.model.CollectionType
import com.brohit.plantdoctor.domain.model.Plant
import com.brohit.plantdoctor.domain.model.PlantCollection
import java.util.Locale


fun PlantDTO.toModel() = Plant(
    name ?: "",
    imageUrl ?: Constants.IMG_URL,
    isHealthy,
    status ?: "",
    tagLine ?: "",
    tags ?: emptySet(),
    id,
    about ?: ""
)

fun PlantCollectionDTO.toModel() = PlantCollection(
    id,
    name ?: "Plants",
    plants?.map { it.toModel() } ?: emptyList(),
    if (type == null || type == 0) CollectionType.Normal else CollectionType.Highlight
)

val IN: Locale
    get() = Locale("en", "IN")