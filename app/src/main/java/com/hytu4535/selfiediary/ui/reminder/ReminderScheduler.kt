package com.hytu4535.selfiediary.ui.reminder

import android.content.Context
import androidx.work.*
import com.hytu4535.selfiediary.notifications.NotificationHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationHelper: NotificationHelper
) {

    fun scheduleReminder(hour: Int, minute: Int) {
        val currentTime = System.currentTimeMillis()
        val scheduledTime = calculateScheduledTime(hour, minute)
        val delay = scheduledTime - currentTime

        val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "selfie_daily_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            reminderRequest
        )
    }

    fun cancelReminder() {
        WorkManager.getInstance(context).cancelUniqueWork("selfie_daily_reminder")
    }

    private fun calculateScheduledTime(hour: Int, minute: Int): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
        calendar.set(java.util.Calendar.MINUTE, minute)
        calendar.set(java.util.Calendar.SECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }
}

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        // TODO: Check if user has taken selfie today
        // If not, send notification
        return Result.success()
    }
}

