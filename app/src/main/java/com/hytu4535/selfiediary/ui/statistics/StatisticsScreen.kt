package com.hytu4535.selfiediary.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hytu4535.selfiediary.data.local.dao.EmojiCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val statistics by viewModel.statistics.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thống kê") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overview card
                item {
                    OverviewCard(
                        totalCount = statistics.totalCount,
                        editedCount = statistics.editedCount,
                        currentMonthCount = statistics.currentMonthCount,
                        currentYearCount = statistics.currentYearCount
                    )
                }

                // Monthly breakdown
                item {
                    MonthlyBreakdownCard(
                        monthlyCount = statistics.monthlyCount
                    )
                }

                // Most used emojis
                if (statistics.mostUsedEmojis.isNotEmpty()) {
                    item {
                        MostUsedEmojisCard(
                            emojis = statistics.mostUsedEmojis
                        )
                    }
                }

                // Additional stats
                item {
                    AdditionalStatsCard(
                        editedPercentage = if (statistics.totalCount > 0) {
                            (statistics.editedCount.toFloat() / statistics.totalCount * 100).toInt()
                        } else 0,
                        lastMonthCount = statistics.lastMonthCount,
                        currentMonthCount = statistics.currentMonthCount
                    )
                }
            }
        }
    }
}

@Composable
private fun OverviewCard(
    totalCount: Int,
    editedCount: Int,
    currentMonthCount: Int,
    currentYearCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tổng quan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.Photo,
                    label = "Tổng số ảnh",
                    value = totalCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    icon = Icons.Default.Edit,
                    label = "Đã chỉnh sửa",
                    value = editedCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.CalendarMonth,
                    label = "Tháng này",
                    value = currentMonthCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    icon = Icons.Default.CalendarToday,
                    label = "Năm nay",
                    value = currentYearCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MonthlyBreakdownCard(
    monthlyCount: Map<Int, Int>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Phân bổ theo tháng",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (monthlyCount.isEmpty()) {
                Text(
                    text = "Chưa có dữ liệu",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val monthNames = listOf(
                    "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
                    "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
                    "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
                )

                (1..12).forEach { month ->
                    val count = monthlyCount[month] ?: 0
                    if (count > 0) {
                        MonthRow(
                            month = monthNames[month - 1],
                            count = count,
                            maxCount = monthlyCount.values.maxOrNull() ?: 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthRow(
    month: String,
    count: Int,
    maxCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = month,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(80.dp)
        )

        LinearProgressIndicator(
            progress = (count.toFloat() / maxCount),
            modifier = Modifier
                .weight(1f)
                .height(24.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun MostUsedEmojisCard(
    emojis: List<EmojiCount>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Emoji phổ biến nhất",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            emojis.take(5).forEach { emojiCount ->
                EmojiRow(
                    emoji = emojiCount.emoji,
                    count = emojiCount.count
                )
            }
        }
    }
}

@Composable
private fun EmojiRow(
    emoji: String,
    count: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "×$count",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AdditionalStatsCard(
    editedPercentage: Int,
    lastMonthCount: Int,
    currentMonthCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Chi tiết khác",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tỷ lệ chỉnh sửa:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$editedPercentage%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            val trend = currentMonthCount - lastMonthCount
            val trendText = when {
                trend > 0 -> "↑ +$trend so với tháng trước"
                trend < 0 -> "↓ $trend so với tháng trước"
                else -> "Giống tháng trước"
            }
            val trendColor = when {
                trend > 0 -> MaterialTheme.colorScheme.primary
                trend < 0 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Xu hướng:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = trendText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = trendColor
                )
            }
        }
    }
}

