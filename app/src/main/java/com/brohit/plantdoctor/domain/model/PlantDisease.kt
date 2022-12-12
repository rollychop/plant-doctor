package com.brohit.plantdoctor.domain.model

data class PlantDisease(
    val name: String,
    val imgUrl: String,
    val aboutDisease: String,
    val solutions: List<String>
)


const val aboutDisease =
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
            "Lorem Ipsum has been the industry's standard dummy text ever since " +
            "the 1500s, when an unknown printer took a galley of type and scrambled " +
            "it to make a type specimen book."
val solutions = listOf("Pest1", "pest2")
val fakeDisease = listOf(
    PlantDisease(
        "Early blight",
        "",
        aboutDisease,
        solutions
    ),
    PlantDisease(
        "Late blight",
        "",
        aboutDisease,
        solutions
    ),
    PlantDisease(
        "Bacterial Spot",
        "",
        aboutDisease,
        solutions
    )

)