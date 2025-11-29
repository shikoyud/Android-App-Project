package com.hytu4535.selfiediary.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hytu4535.selfiediary.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

const val BASE_URI = "app://com.hytu4535.selfiediary"
const val REMINDER_ROUTE = "capture"

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CHANNEL_ID = "selfie_reminder_channel"
        private const val CHANNEL_NAME = "Nhắc nhở Selfie"
        private const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
    }
    // Deep link directly to the Capture screen so tapping the notification opens camera
    val deepLinkUri = Uri.parse("$BASE_URI/$REMINDER_ROUTE")
    val intent = Intent(
        Intent.ACTION_VIEW,
        deepLinkUri,
        context,
        MainActivity::class.java
    )
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Nhắc nhở chụp selfie hàng ngày"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showReminderNotification(title: String, message: String) {
        // TODO: Implement notification display logic
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
