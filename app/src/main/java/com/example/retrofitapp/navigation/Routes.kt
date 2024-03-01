package com.example.retrofitapp.navigation

sealed class Routes(val route: String) {
    object ListScreen : Routes("ListScreen")
    object FavsScreen : Routes("FavsScreen")
    object DetailScreen : Routes("DetailScreen")
}