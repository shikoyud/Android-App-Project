package com.hytu4535.selfiediary.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderSettingsScreen(
    onBack: () -> Unit
) {
    var reminderEnabled by remember { mutableStateOf(true) }
    var selectedHour by remember { mutableStateOf(8) }
    var selectedMinute by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt nhắc nhở") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
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
                    onCheckedChange = { reminderEnabled = it }
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
                            text = String.format("%02d:%02d", selectedHour, selectedMinute),
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
                                        text = String.format("%02d", selectedHour),
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
                                        selectedMinute = if (selectedMinute >= 15) selectedMinute - 15 else 45
                                    }) {
                                        Text("-")
                                    }
                                    Text(
                                        text = String.format("%02d", selectedMinute),
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(onClick = {
                                        selectedMinute = if (selectedMinute < 45) selectedMinute + 15 else 0
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
                        // TODO: Save reminder settings
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lưu cài đặt")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "💡 Lưu ý: Đảm bảo ứng dụng có quyền gửi thông báo để nhận nhắc nhở.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

