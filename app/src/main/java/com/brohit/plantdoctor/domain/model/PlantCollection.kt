package com.brohit.plantdoctor.domain.model


data class PlantCollection(
    val id: Long,
    val name: String,
    val plantList: List<Plant>,
    val type: CollectionType = CollectionType.Normal
)

enum class CollectionType { Normal, Highlight }

/**
 * A fake repo
 */
object PlantRepoStatic {
    fun getPlants(): List<PlantCollection> = plantCollections
}

/**
 * Static data
 */

private val vegetableCollection = PlantCollection(
    id = 1L,
    name = "Hot Plants",
    type = CollectionType.Highlight,
    plantList = plants.subList(0, 5)
)

private val fruitCollection = PlantCollection(
    id = 2L,
    name = "Fruit Plant",
    plantList = plants.subList(5, 10)
)


private val plantCollections = listOf(
    vegetableCollection,
    fruitCollection,
)


