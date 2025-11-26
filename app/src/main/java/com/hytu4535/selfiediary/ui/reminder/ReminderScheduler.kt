package com.hytu4535.selfiediary.ui.reminder

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.hytu4535.selfiediary.domain.usecase.HasSelfieTodayUseCase
import com.hytu4535.selfiediary.notifications.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
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
                    .setRequiresDeviceIdle(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "selfie_daily_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
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