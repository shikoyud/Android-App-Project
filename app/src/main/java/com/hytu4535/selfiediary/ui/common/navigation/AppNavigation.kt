package com.hytu4535.selfiediary.ui.common.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hytu4535.selfiediary.ui.capture.CaptureScreen
import com.hytu4535.selfiediary.ui.detail.DetailScreen
import com.hytu4535.selfiediary.ui.edit.EditScreen
import com.hytu4535.selfiediary.ui.filter.DateFilterScreen
import com.hytu4535.selfiediary.ui.gallery.GalleryScreen
import com.hytu4535.selfiediary.ui.home.HomeScreen
import com.hytu4535.selfiediary.ui.search.SearchScreen
import com.hytu4535.selfiediary.ui.settings.SettingsScreen
import com.hytu4535.selfiediary.ui.settings.ReminderSettingsScreen
import com.hytu4535.selfiediary.ui.statistics.StatisticsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCapture = { navController.navigate(Screen.Capture.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToStatistics = { navController.navigate(Screen.Statistics.route) },
                onNavigateToDateFilter = { navController.navigate(Screen.DateFilter.route) },
                onNavigateToDetail = { selfieId ->
                    navController.navigate(Screen.Detail.createRoute(selfieId))
                },
                navController = navController
            )
        }

        composable(Screen.Capture.route) {
            CaptureScreen(
                onBack = { navController.popBackStack() },
                onCaptureSuccess = { imagePath ->
                    navController.navigate(Screen.Edit.createRoute(imagePath))
                }
            )
        }

        composable(
            route = Screen.Edit.route,
            arguments = listOf(
                navArgument("imagePath") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val encodedPath = backStackEntry.arguments?.getString("imagePath")
            val imagePath = encodedPath?.let { Uri.decode(it) } ?: run {
                return@composable
            }
            EditScreen(
                imagePath = imagePath,
                onBack = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Gallery.route) {
            GalleryScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { selfieId ->
                    navController.navigate(Screen.Detail.createRoute(selfieId))
                }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.DateFilter.route) {
            DateFilterScreen(
                onBack = { navController.popBackStack() },
                onDateSelected = { start, end ->
                    // Navigate back with result - will be handled by HomeScreen
                    navController.previousBackStackEntry?.savedStateHandle?.set("date_range", Pair(start, end))
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("selfieId") { type = NavType.LongType }
            )
        ) {
            DetailScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToReminderSettings = {
                    navController.navigate(Screen.ReminderSettings.route)
                }
            )
        }

        composable(Screen.ReminderSettings.route) {
            ReminderSettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

