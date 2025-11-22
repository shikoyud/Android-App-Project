package com.hytu4535.selfiediary.ui.common.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Capture : Screen("capture")
    object Gallery : Screen("gallery")
    object Settings : Screen("settings")
    object Detail : Screen("detail/{selfieId}") {
        fun createRoute(selfieId: Long) = "detail/$selfieId"
    }
    object ImageDetail : Screen("image_detail/{imageId}") {
        fun createRoute(imageId: Long) = "image_detail/$imageId"
    }
    object ReminderSettings : Screen("reminder_settings")
}

