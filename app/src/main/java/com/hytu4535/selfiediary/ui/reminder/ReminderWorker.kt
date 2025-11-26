package com.hytu4535.selfiediary.ui.reminder

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hytu4535.selfiediary.domain.usecase.HasSelfieTodayUseCase
import com.hytu4535.selfiediary.notifications.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val hasSelfieTodayUseCase: HasSelfieTodayUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val hasTakenSelfieToday = hasSelfieTodayUseCase()

            if (!hasTakenSelfieToday) {
                notificationHelper.showReminderNotification(
                    title = "Đừng quên chụp Selfie hôm nay! 📸",
                    message = "Ghi lại khoảnh khắc của bạn ngay bây giờ!"
                )
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
