package com.hytu4535.selfiediary.ui.common.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Capture : Screen("capture")
    object Edit : Screen("edit/{imagePath}") {
        fun createRoute(imagePath: String) = "edit/${Uri.encode(imagePath)}"
    }
    object Gallery : Screen("gallery")
    object Settings : Screen("settings")
    object ImageDetail : Screen("image_detail/{imageId}") {
        fun createRoute(imageId: Long) = "image_detail/$imageId"
    }
    object ReminderSettings : Screen("reminder_settings")
}

