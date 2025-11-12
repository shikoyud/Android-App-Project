package com.hytu4535.selfiediary.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToReminderSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cài đặt") },
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
        ) {
            // Reminder Settings
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Cài đặt nhắc nhở",
                subtitle = "Thiết lập thời gian nhắc nhở hàng ngày",
                onClick = onNavigateToReminderSettings
            )

            Divider()

            // Time-lapse Video
            SettingsItem(
                icon = Icons.Default.VideoLibrary,
                title = "Tạo video Time-lapse",
                subtitle = "Ghép các ảnh thành video",
                onClick = { /* TODO */ }
            )

            Divider()

            // Backup & Sync
            SettingsItem(
                icon = Icons.Default.CloudUpload,
                title = "Sao lưu & Đồng bộ",
                subtitle = "Google Drive, Dropbox",
                onClick = { /* TODO */ }
            )

            Divider()

            // Security Settings
            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Cài đặt bảo mật",
                subtitle = "Khóa ứng dụng bằng PIN hoặc vân tay",
                onClick = { /* TODO */ }
            )

            Divider()

            Spacer(modifier = Modifier.weight(1f))

            // App Version
            Text(
                text = "Phiên bản 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Xem chi tiết",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

