package com.hytu4535.selfiediary.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hytu4535.selfiediary.ui.detail.components.EmojiPicker
import com.hytu4535.selfiediary.ui.detail.components.NoteEditor
import com.hytu4535.selfiediary.ui.detail.components.ZoomableImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val currentSelfie by viewModel.currentSelfie.collectAsState()
    val showNoteEditor by viewModel.showNoteEditor.collectAsState()
    val showEmojiPicker by viewModel.showEmojiPicker.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val allSelfies by viewModel.allSelfies.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    var showCard by remember { mutableStateOf(true) }
    // Swipe gesture detection
    var offsetX by remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            if (showCard) {
                TopAppBar(
                    title = {
                        Text(
                            text = "${currentIndex + 1} / ${allSelfies.size}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa")
                        }
                        IconButton(onClick = { /* TODO: Share */ }) {
                            Icon(Icons.Default.Share, contentDescription = "Chia sẻ")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        },
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            currentSelfie?.let { selfie ->
                // Main Image with zoom and swipe
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures (
                                onTap = {
                                    showCard = !showCard
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX > 100) {
                                        viewModel.navigateToPrevious()
                                    } else if (offsetX < -100) {
                                        viewModel.navigateToNext()
                                    }
                                    offsetX = 0f
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    offsetX += dragAmount
                                }
                            )
                        }
                ) {
                    ZoomableImage(
                        imageUrl = selfie.filePath,
                        contentDescription = "Selfie detail"
                    )
                }

                // Bottom Info Card
                if (showCard) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Date and Time
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = formatDate(selfie.timestamp),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = formatTime(selfie.timestamp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }

                                // Emoji display
                                if (selfie.emoji != null) {
                                    Text(
                                        text = selfie.emoji,
                                        style = MaterialTheme.typography.displaySmall,
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.primaryContainer,
                                                CircleShape
                                            )
                                            .padding(8.dp)
                                    )
                                }
                            }

                            // Note
                            if (selfie.note.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = selfie.note,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Action Buttons
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.showNoteEditor() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ghi chú")
                                }

                                OutlinedButton(
                                    onClick = { viewModel.showEmojiPicker() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        Icons.Default.EmojiEmotions,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Cảm xúc")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Note Editor Dialog
    if (showNoteEditor) {
        NoteEditor(
            currentNote = currentSelfie?.note ?: "",
            onDismiss = { viewModel.hideNoteEditor() },
            onSave = { newNote ->
                viewModel.updateNote(newNote)
                viewModel.hideNoteEditor()
            }
        )
    }

    // Emoji Picker Dialog
    if (showEmojiPicker) {
        EmojiPicker(
            currentEmoji = currentSelfie?.emoji,
            onDismiss = { viewModel.hideEmojiPicker() },
            onSelect = { emoji ->
                viewModel.updateEmoji(emoji)
                viewModel.hideEmojiPicker()
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc muốn xóa ảnh này? Hành động này không thể hoàn tác.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCurrent(onDeleted = onBack)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Xóa", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("vi"))
    return dateFormat.format(Date(timestamp))
}

private fun formatTime(timestamp: Long): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(Date(timestamp))
}

