package com.brohit.plantdoctor.domain.model


data class PlantCollection(
    val id: Long,
    val name: String,
    val snacks: List<Plant>,
    val type: CollectionType = CollectionType.Normal
)

enum class CollectionType { Normal, Highlight }

/**
 * A fake repo
 */
object PlantRepoStatic {
    fun getPlants(): List<PlantCollection> = snackCollections
}

/**
 * Static data
 */

private val tastyTreats = PlantCollection(
    id = 1L,
    name = "Vegetable Plant",
    type = CollectionType.Highlight,
    snacks = plants.subList(0, 5)
)

private val popular = PlantCollection(
    id = 2L,
    name = "Fruit Plant",
    snacks = plants.subList(5, 10)
)


private val snackCollections = listOf(
    tastyTreats,
    popular,
)


