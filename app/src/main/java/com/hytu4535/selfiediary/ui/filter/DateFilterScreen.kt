package com.hytu4535.selfiediary.ui.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilterScreen(
    onBack: () -> Unit,
    onDateSelected: (Long, Long) -> Unit
) {
    var selectedYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var showYearPicker by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lọc theo ngày") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Year selector
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showYearPicker = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Năm", style = MaterialTheme.typography.titleMedium)
                    Text(
                        selectedYear.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Month selector
            Text("Chọn tháng:", style = MaterialTheme.typography.titleMedium)

            // Month grid (3 columns x 4 rows)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0..3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0..2) {
                            val month = row * 3 + col + 1
                            if (month <= 12) {
                                MonthCard(
                                    month = month,
                                    isSelected = month == selectedMonth,
                                    onClick = {
                                        selectedMonth = month
                                        val (start, end) = getMonthRange(selectedYear, month)
                                        onDateSelected(start, end)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Quick filters
            Text("Lọc nhanh:", style = MaterialTheme.typography.titleMedium)

            Button(
                onClick = {
                    val (start, end) = getTodayRange()
                    onDateSelected(start, end)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hôm nay")
            }

            Button(
                onClick = {
                    val (start, end) = getThisWeekRange()
                    onDateSelected(start, end)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tuần này")
            }

            Button(
                onClick = {
                    val (start, end) = getThisMonthRange()
                    onDateSelected(start, end)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tháng này")
            }

            Button(
                onClick = {
                    val (start, end) = getThisYearRange()
                    onDateSelected(start, end)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Năm nay")
            }
        }

        // Year picker dialog
        if (showYearPicker) {
            YearPickerDialog(
                currentYear = selectedYear,
                onYearSelected = { year ->
                    selectedYear = year
                    showYearPicker = false
                },
                onDismiss = { showYearPicker = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonthCard(
    month: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthNames = listOf(
        "T1", "T2", "T3", "T4", "T5", "T6",
        "T7", "T8", "T9", "T10", "T11", "T12"
    )

    Card(
        modifier = modifier
            .aspectRatio(1.2f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = monthNames[month - 1],
                style = MaterialTheme.typography.titleLarge,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun YearPickerDialog(
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val years = (2020..2030).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn năm") },
        text = {
            LazyColumn {
                items(years) { year ->
                    TextButton(
                        onClick = { onYearSelected(year) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = year.toString(),
                            style = if (year == currentYear) {
                                MaterialTheme.typography.titleLarge
                            } else {
                                MaterialTheme.typography.bodyLarge
                            },
                            color = if (year == currentYear) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

// Helper functions
private fun getMonthRange(year: Int, month: Int): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    // Start of month
    calendar.set(year, month - 1, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

    // End of month
    calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val end = calendar.timeInMillis

    return start to end
}

private fun getTodayRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    // Start of today
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

    // End of today
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val end = calendar.timeInMillis

    return start to end
}

private fun getThisWeekRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    // Start of week (Monday)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

    // End of week (Sunday)
    calendar.add(Calendar.DAY_OF_WEEK, 6)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val end = calendar.timeInMillis

    return start to end
}

private fun getThisMonthRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    return getMonthRange(year, month)
}

private fun getThisYearRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)

    // Start of year
    calendar.set(year, 0, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

    // End of year
    calendar.set(year, 11, 31, 23, 59, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val end = calendar.timeInMillis

    return start to end
}

