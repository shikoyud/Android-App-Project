package com.hytu4535.selfiediary.ui.common.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Capture : Screen("capture")
    object Edit : Screen("edit/{imagePath}") {
        fun createRoute(imagePath: String) = "edit/${Uri.encode(imagePath)}"
    }
    object Gallery : Screen("gallery")
    object Search : Screen("search")
    object Statistics : Screen("statistics")
    object DateFilter : Screen("date_filter")
    object Settings : Screen("settings")
    object Detail : Screen("detail/{selfieId}") {
        fun createRoute(selfieId: Long) = "detail/$selfieId"
    }
    object ImageDetail : Screen("image_detail/{imageId}") {
        fun createRoute(imageId: Long) = "image_detail/$imageId"
    }
    object ReminderSettings : Screen("reminder_settings")
}

