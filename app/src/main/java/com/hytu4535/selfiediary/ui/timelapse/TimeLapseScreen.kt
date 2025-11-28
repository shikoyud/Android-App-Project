@file:Suppress("DEPRECATION")

package com.hytu4535.selfiediary.ui.timelapse

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.VideoView
import android.widget.MediaController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLapseScreen(
    onBack: () -> Unit,
    viewModel: TimeLapseViewModel = hiltViewModel()
) {
    var frameRate by remember { mutableStateOf("10") }
    val images by viewModel.images.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val outputPath by viewModel.outputPath.collectAsState()
    val previewPath by viewModel.previewPath.collectAsState()
    val errorMessage by viewModel.errorMessageFlow().collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Show error or success snackbar when state changes
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(outputPath) {
        outputPath?.let { path ->
            snackbarHostState.showSnackbar("Video đã lưu: $path")
        }
    }

    // When previewPath is set, show preview dialog
    if (previewPath != null) {
        AlertDialog(
            onDismissRequest = { viewModel.discardPreview() },
            confirmButton = {
                Button(onClick = { viewModel.savePreview() }) { Text("Lưu") }
            },
            dismissButton = {
                Button(onClick = { viewModel.discardPreview() }) { Text("Hủy") }
            },
            title = { Text("Xem trước video Time-lapse") },
            text = {
                AndroidView(factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoPath(previewPath)
                        val mc = MediaController(ctx)
                        mc.setAnchorView(this)
                        setMediaController(mc)
                        requestFocus()
                        start()
                    }
                }, modifier = Modifier.fillMaxWidth().height(300.dp))
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tạo video Time-lapse") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Chọn khoảng thời gian:", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val (start, end) = getTodayRange()
                    viewModel.loadRange(start, end)
                }) {
                    Text("Hôm nay")
                }
                Button(onClick = {
                    val (start, end) = getThisWeekRange()
                    viewModel.loadRange(start, end)
                }) {
                    Text("Tuần")
                }
                Button(onClick = {
                    val (start, end) = getThisMonthRange()
                    viewModel.loadRange(start, end)
                }) {
                    Text("Tháng")
                }
            }

            Text("Ảnh tìm được: ${images.size}")

            OutlinedTextField(
                value = frameRate,
                onValueChange = { frameRate = it.filter { ch -> ch.isDigit() } },
                label = { Text("Frame rate (fps)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (isProcessing) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Đang tạo video…")
                }
            }

            Button(
                onClick = {
                    val fps = frameRate.toIntOrNull() ?: 10
                    viewModel.createTimeLapse(fps)
                },
                enabled = images.isNotEmpty() && !isProcessing
            ) {
                Text("Tạo video Time-lapse")
            }

            outputPath?.let { path ->
                Text("Video đã tạo: $path", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Lưu ý: Ảnh được lấy từ thư viện ảnh đã lưu trong app.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun getTodayRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val end = calendar.timeInMillis

    return start to end
}

private fun getThisWeekRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

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

private fun getMonthRange(year: Int, month: Int): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    calendar.set(year, month - 1, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

    calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val end = calendar.timeInMillis

    return start to end
}
