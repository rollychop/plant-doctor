package com.brohit.plantdoctor.domain.model

data class Plant(
    val name: String,
    val imageUrl: String,
    val isHealthy: Boolean = true,
    val status: String = "",
    val tagLine: String = "",
    val tags: Set<String> = emptySet(),
    val id: Long = 0L,
    val about: String = ""
)

val plants = listOf(
    Plant(
        id = 1L,
        name = "Potato",
        imageUrl = "https://images.unsplash.com/photo-1593708697557-f2feca483132?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80",
    ),
    Plant(

        "Tomato",
        imageUrl = "https://images.unsplash.com/photo-1471194402529-8e0f5a675de6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
    ),
    Plant(
        "Corn",
        imageUrl = "https://images.unsplash.com/photo-1601593768799-7433c2447c29?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
        id = 2L
    ),
    Plant(
        "Raspberry",
        imageUrl = "https://images.unsplash.com/photo-1596591868231-05e852efc96f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=396&q=80",
        id = 3L
    ),
    Plant(
        "Peach",
        imageUrl = "https://images.unsplash.com/photo-1595743825637-cdafc8ad4173?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80",
        id = 4L
    ),
    Plant(
        "Strawberry",
        imageUrl = "https://images.unsplash.com/photo-1588165171080-c89acfa5ee83?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80",
        id = 5L
    ),
    Plant(
        "Apple",
        imageUrl = "https://images.unsplash.com/photo-1640193272671-5f9c81ac92ac?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
        id = 6L
    ),
    Plant(
        "Pepper",
        imageUrl = "https://images.unsplash.com/photo-1606374067068-2d031aed64e1?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1172&q=80",
        id = 7L
    ),
    Plant(
        "Cherry",
        imageUrl = "https://images.unsplash.com/photo-1621763668206-b9a47fc8727a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=735&q=80",
        id = 8L
    ),
    Plant(
        "Grape",
        imageUrl = "https://images.unsplash.com/photo-1602330102257-04c00af50c1a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=327&q=80",
        id = 9L
    ),
    Plant(
        "Orange",
        imageUrl = "https://images.unsplash.com/photo-1583063339941-694e94523a66?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=388&q=80",
        id = 10L
    ),
)