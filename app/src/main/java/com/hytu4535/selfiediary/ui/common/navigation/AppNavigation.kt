package com.hytu4535.selfiediary.ui.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hytu4535.selfiediary.ui.capture.CaptureScreen
import com.hytu4535.selfiediary.ui.gallery.GalleryScreen
import com.hytu4535.selfiediary.ui.home.HomeScreen
import com.hytu4535.selfiediary.ui.settings.SettingsScreen
import com.hytu4535.selfiediary.ui.settings.ReminderSettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCapture = { navController.navigate(Screen.Capture.route) },
                onNavigateToGallery = { navController.navigate(Screen.Gallery.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Capture.route) {
            CaptureScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Gallery.route) {
            GalleryScreen(onBack = { navController.popBackStack() })
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

