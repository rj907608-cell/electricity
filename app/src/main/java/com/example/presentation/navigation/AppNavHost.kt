package com.example.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ElectricApplication
import com.example.presentation.inventory.InventoryScreen
import com.example.presentation.inventory.InventoryViewModel
import com.example.presentation.project_detail.ProjectDetailScreen
import com.example.presentation.project_detail.ProjectDetailViewModel
import com.example.presentation.projects.ProjectsScreen
import com.example.presentation.projects.ProjectsViewModel
import com.example.presentation.sector_items.SectorItemsScreen
import com.example.presentation.sector_items.SectorItemsViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as ElectricApplication
    val container = app.container

    NavHost(
        navController = navController,
        startDestination = Screen.Projects.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {
        // 1. Projects List
        composable(Screen.Projects.route) {
            val projectsViewModel = remember { ProjectsViewModel(container) }
            ProjectsScreen(
                viewModel = projectsViewModel,
                onNavigateToProject = { projectId ->
                    navController.navigate(Screen.ProjectDetail.createRoute(projectId))
                }
            )
        }

        // 2. Project Detail (Sectors)
        composable(
            route = Screen.ProjectDetail.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val viewModel = remember(projectId) {
                ProjectDetailViewModel(projectId = projectId, container = container)
            }
            ProjectDetailScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSector = { sectorId, sectorName ->
                    navController.navigate(Screen.SectorItems.createRoute(sectorId, sectorName))
                },
                onNavigateToInventory = { pId ->
                    navController.navigate(Screen.Inventory.createRoute(pId))
                }
            )
        }

        // 3. Sector Supplies (Items & Quantities)
        composable(
            route = Screen.SectorItems.route,
            arguments = listOf(
                navArgument("sectorId") { type = NavType.LongType },
                navArgument("sectorName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sectorId = backStackEntry.arguments?.getLong("sectorId") ?: return@composable
            val rawName = backStackEntry.arguments?.getString("sectorName") ?: "قطاع"
            val sectorName = java.net.URLDecoder.decode(rawName, "UTF-8")

            val viewModel = remember(sectorId) {
                SectorItemsViewModel(sectorId = sectorId, sectorName = sectorName, container = container)
            }
            SectorItemsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 4. Inventory / Invoice
        composable(
            route = Screen.Inventory.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
            val viewModel = remember(projectId) {
                InventoryViewModel(projectId = projectId, container = container)
            }
            InventoryScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
