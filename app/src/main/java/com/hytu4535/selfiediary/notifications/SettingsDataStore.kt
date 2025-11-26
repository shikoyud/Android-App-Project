package com.hytu4535.selfiediary.notifications

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reminder_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
    }

    companion object {
        const val DEFAULT_HOUR = 8
        const val DEFAULT_MINUTE = 0
    }

    val reminderSettings: Flow<ReminderSettings> = context.dataStore.data
        .map { preferences ->
            ReminderSettings(
                enabled = preferences[PreferencesKeys.REMINDER_ENABLED] ?: true,
                hour = preferences[PreferencesKeys.REMINDER_HOUR] ?: DEFAULT_HOUR,
                minute = preferences[PreferencesKeys.REMINDER_MINUTE] ?: DEFAULT_MINUTE
            )
        }

    suspend fun saveReminderSettings(enabled: Boolean, hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_ENABLED] = enabled
            preferences[PreferencesKeys.REMINDER_HOUR] = hour
            preferences[PreferencesKeys.REMINDER_MINUTE] = minute
        }
    }

    suspend fun disableReminder() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_ENABLED] = false
        }
    }
}

data class ReminderSettings(
    val enabled: Boolean,
    val hour: Int,
    val minute: Int
)
