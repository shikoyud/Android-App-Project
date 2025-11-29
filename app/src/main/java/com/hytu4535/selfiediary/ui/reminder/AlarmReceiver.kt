package com.hytu4535.selfiediary.ui.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.hytu4535.selfiediary.notifications.NotificationHelper
import com.hytu4535.selfiediary.util.Logger
import java.util.*

const val ACTION_REMINDER_ALARM = "com.hytu4535.selfiediary.action.REMINDER_ALARM"
const val EXTRA_REMINDER_HOUR = "extra_reminder_hour"
const val EXTRA_REMINDER_MINUTE = "extra_reminder_minute"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            val hour = intent?.getIntExtra(EXTRA_REMINDER_HOUR, -1) ?: -1
            val minute = intent?.getIntExtra(EXTRA_REMINDER_MINUTE, -1) ?: -1

            // Instead of calling suspend DB method here, delegate to SimpleReminderWorker
            val work = OneTimeWorkRequestBuilder<SimpleReminderWorker>().build()
            Log.d("AlarmReceiver", "Alarm received for $hour:$minute - enqueuing SimpleReminderWorker")
            Logger.append(context, "Alarm received for $hour:$minute - enqueuing SimpleReminderWorker")
            WorkManager.getInstance(context).enqueue(work)

            // Reschedule next day's exact alarm if hour/min provided
            if (hour >= 0 && minute >= 0) {
                Log.d("AlarmReceiver", "Rescheduling next day alarm for $hour:$minute")
                Logger.append(context, "Rescheduling next day alarm for $hour:$minute")
                ReminderScheduler.scheduleExactAlarm(context, hour, minute)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
