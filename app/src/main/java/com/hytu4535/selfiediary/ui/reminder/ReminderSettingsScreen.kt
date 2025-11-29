package com.hytu4535.selfiediary.ui.reminder

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderSettingsScreen(
    onBack: () -> Unit,
    viewModel: ReminderSettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val settingsState by viewModel.reminderSettings.collectAsStateWithLifecycle()

    var reminderEnabled by remember { mutableStateOf(settingsState.enabled) }
    var selectedHour by remember { mutableStateOf(settingsState.hour) }
    var selectedMinute by remember { mutableStateOf(settingsState.minute) }


    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationPermission = granted
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    LaunchedEffect(settingsState) {
        reminderEnabled = settingsState.enabled
        selectedHour = settingsState.hour
        selectedMinute = settingsState.minute
    }

    // Check if exact alarms can be scheduled (API 31+)
    val canScheduleExact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val am = context.getSystemService(android.app.AlarmManager::class.java)
        am?.canScheduleExactAlarms() ?: true
    } else true

    val exactLauncher = rememberLauncherForActivityResult(StartActivityForResult()) { _ ->
        // no-op; user returns from system UI
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt nhắc nhở") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Enable/Disable Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Bật nhắc nhở hàng ngày",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Nhắc bạn chụp selfie mỗi ngày",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = { isEnabled ->
                        reminderEnabled = isEnabled
                        viewModel.toggleReminder(isEnabled, selectedHour, selectedMinute)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (reminderEnabled) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Thời gian nhắc nhở",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Time Display
                        Text(
                            text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute),
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hour and Minute Selectors
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Hour Selector
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Giờ", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    IconButton(onClick = {
                                        selectedHour = if (selectedHour > 0) selectedHour - 1 else 23
                                    }) {
                                        Text("-")
                                    }
                                    Text(
                                        text = String.format(Locale.getDefault(), "%02d", selectedHour),
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(onClick = {
                                        selectedHour = if (selectedHour < 23) selectedHour + 1 else 0
                                    }) {
                                        Text("+")
                                    }
                                }
                            }

                            // Minute Selector
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Phút", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    IconButton(onClick = {
                                        selectedMinute = if (selectedMinute > 0) selectedMinute - 1 else 59
                                    }) {
                                        Text("-")
                                    }
                                    Text(
                                        text = String.format(Locale.getDefault(), "%02d", selectedMinute),
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(onClick = {
                                        selectedMinute = if (selectedMinute < 59) selectedMinute + 1 else 0
                                    }) {
                                        Text("+")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.saveSettings(reminderEnabled, selectedHour, selectedMinute)
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = reminderEnabled
                ) {
                    Text("Lưu cài đặt")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // If system disallows exact alarms, show button to request/guide user
                if (!canScheduleExact) {
                    Button(onClick = {
                        // Open system UI to request exact alarm permission if available
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {}
                            exactLauncher.launch(intent)
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Yêu cầu quyền Exact Alarms")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nếu thiết bị chặn exact alarms, hãy bấm nút trên để mở cài đặt hệ thống và cho phép; sau đó bật nhắc nhở lại.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = "💡 Lưu ý: Đảm bảo ứng dụng có quyền gửi thông báo để nhận nhắc nhở.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
