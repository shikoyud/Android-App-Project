package com.hytu4535.selfiediary.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.notifications.ReminderSettings
import com.hytu4535.selfiediary.notifications.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderSettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    val reminderSettings: StateFlow<ReminderSettings> = settingsDataStore.reminderSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReminderSettings(
                enabled = true,
                hour = SettingsDataStore.DEFAULT_HOUR,
                minute = SettingsDataStore.DEFAULT_MINUTE
            )
        )

    fun saveSettings(enabled: Boolean, hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsDataStore.saveReminderSettings(enabled, hour, minute)

            if (enabled) {
                reminderScheduler.scheduleReminder(hour, minute)
            } else {
                reminderScheduler.cancelReminder()
            }
        }
    }

    fun toggleReminder(enabled: Boolean, currentHour: Int, currentMinute: Int) {
        viewModelScope.launch {
            if (enabled) {
                settingsDataStore.saveReminderSettings(true, currentHour, currentMinute)
                reminderScheduler.scheduleReminder(currentHour, currentMinute)
            } else {
                settingsDataStore.disableReminder()
                reminderScheduler.cancelReminder()
            }
        }
    }
}