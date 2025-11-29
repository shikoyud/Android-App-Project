package com.hytu4535.selfiediary.ui.reminder

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hytu4535.selfiediary.notifications.NotificationHelper
import com.hytu4535.selfiediary.data.local.db.AppDatabase
import com.hytu4535.selfiediary.util.Logger
import java.util.*

/**
 * Simple non-Hilt worker that checks DB whether a selfie was taken today before showing notification.
 */
class SimpleReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        try {
            // Compute start and end of today
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = cal.timeInMillis
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            cal.set(Calendar.MILLISECOND, 999)
            val endOfDay = cal.timeInMillis

            // Access Room DB directly
            val db = AppDatabase.getInstance(applicationContext)
            val count = db.selfieDao().hasSelfieToday(startOfDay, endOfDay)

            if (count > 0) {
                // User already took selfie today -> skip notification
                Log.d("SimpleReminderWorker", "Skipping notification because selfie count=$count")
                Logger.append(applicationContext, "SimpleReminderWorker: Skipping notification because selfie count=$count")
                return Result.success()
            }

            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.showReminderNotification(
                title = "Đừng quên chụp Selfie hôm nay! 📸",
                message = "Ghi lại khoảnh khắc của bạn ngay bây giờ!"
            )
            Log.d("SimpleReminderWorker", "Notification shown (no selfie today)")
            Logger.append(applicationContext, "SimpleReminderWorker: Notification shown (no selfie today)")
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
    }
}
