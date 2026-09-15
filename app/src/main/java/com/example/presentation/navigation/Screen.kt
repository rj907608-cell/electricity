package com.example.presentation.navigation

sealed class Screen(val route: String) {
    object Projects : Screen("projects")

    object ProjectDetail : Screen("project_detail/{projectId}") {
        fun createRoute(projectId: Long): String = "project_detail/$projectId"
    }

    object SectorItems : Screen("sector_items/{sectorId}/{sectorName}") {
        fun createRoute(sectorId: Long, sectorName: String): String =
            "sector_items/$sectorId/${java.net.URLEncoder.encode(sectorName, "UTF-8")}"
    }

    object Inventory : Screen("inventory/{projectId}") {
        fun createRoute(projectId: Long): String = "inventory/$projectId"
    }
}
