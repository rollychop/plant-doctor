package com.brohit.plantdoctor.presentation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object DetailScreen : Screen("detail_screen")
}
