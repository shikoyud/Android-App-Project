package com.hytu4535.selfiediary.ui.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.hytu4535.selfiediary.util.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import java.util.*

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun scheduleReminder(hour: Int, minute: Int) {
        // Use exact AlarmManager to ensure delivery even when app is not running
        scheduleExactAlarm(context, hour, minute)
    }

    fun cancelReminder() {
        cancelExactAlarm(context)
    }

    /**
     * For quick local testing: enqueue a one-time worker that will run immediately.
     */
    fun scheduleImmediateTest() {
        val request = OneTimeWorkRequestBuilder<SimpleReminderWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }

    private fun calculateScheduledTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }

    companion object {
        fun scheduleExactAlarm(context: Context, hour: Int, minute: Int) {
            try {
                // On Android 12+ (API 31) apps may not be allowed to schedule exact alarms unless permitted.
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val canExact = alarmManager.canScheduleExactAlarms()
                    Log.d("ReminderScheduler", "canScheduleExactAlarms=$canExact for hour=$hour minute=$minute")
                    Logger.append(context, "canScheduleExactAlarms=$canExact for hour=$hour minute=$minute")
                    if (!canExact) {
                        // fallback: schedule via WorkManager periodic with initial delay
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        cal.set(Calendar.SECOND, 0)
                        if (cal.timeInMillis <= System.currentTimeMillis()) cal.add(Calendar.DAY_OF_YEAR, 1)
                        val delay = cal.timeInMillis - System.currentTimeMillis()
                        val req = PeriodicWorkRequestBuilder<SimpleReminderWorker>(1, TimeUnit.DAYS)
                            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
                            .build()
                        WorkManager.getInstance(context).enqueueUniquePeriodicWork("selfie_daily_reminder", ExistingPeriodicWorkPolicy.REPLACE, req)
                        return
                    }
                }
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                Log.d("ReminderScheduler", "scheduling exact alarm for $hour:$minute")
                Logger.append(context, "scheduling exact alarm for $hour:$minute")
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    action = ACTION_REMINDER_ALARM
                    putExtra(EXTRA_REMINDER_HOUR, hour)
                    putExtra(EXTRA_REMINDER_MINUTE, minute)
                }
                val pi = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                if (calendar.timeInMillis <= System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
                Log.d("ReminderScheduler", "alarm set (exact) for $hour:$minute")
                Logger.append(context, "alarm set (exact) for $hour:$minute")
             } catch (e: Exception) {
                 e.printStackTrace()
             }
         }

         fun cancelExactAlarm(context: Context) {
             try {
                Log.d("ReminderScheduler", "cancelExactAlarm called")
                Logger.append(context, "cancelExactAlarm called")
                 val intent = Intent(context, AlarmReceiver::class.java).apply { action = ACTION_REMINDER_ALARM }
                 val pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
                 if (pi != null) {
                     val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                     alarmManager.cancel(pi)
                     pi.cancel()
                    Log.d("ReminderScheduler", "alarm cancelled")
                    Logger.append(context, "alarm cancelled")
                 }
             } catch (e: Exception) {
                 e.printStackTrace()
             }
         }
     }
 }
