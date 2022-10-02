package com.brohit.plantdoctor.domain.model

data class Plant(
    val name: String,
    val isHealthy: Boolean = true,
    val diseaseName: String? = "",
    val imageUrl: String? = "https://cdn.britannica.com/68/176668-050-3BE5AEA6/buffalo-bur.jpg"
)
