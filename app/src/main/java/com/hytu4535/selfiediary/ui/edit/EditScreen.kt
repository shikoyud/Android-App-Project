@file:Suppress("DEPRECATION")

package com.hytu4535.selfiediary.ui.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CropRotate
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.hytu4535.selfiediary.ui.detail.components.EmojiPicker
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    imagePath: String,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {
    val editState by viewModel.editState.collectAsStateWithLifecycle()
    val currentBaseImagePath by viewModel.baseImagePath.collectAsStateWithLifecycle()
    val currentDisplayImagePath by viewModel.displayImagePath.collectAsStateWithLifecycle()
    var showEmojiDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var selectedTool by remember { mutableStateOf(EditTool.NONE) }
    var cropTriggerPath by remember { mutableStateOf<String?>(null) }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.path?.let { uriPath ->
                viewModel.applyRotation(uriPath)
            }
        }
        cropTriggerPath = null
        selectedTool = EditTool.NONE
    }

    LaunchedEffect(cropTriggerPath) {
        cropTriggerPath?.let { path ->
            val imageUri = Uri.fromFile(File(path))
            val outputUri = viewModel.imageProcessor.createTempImageUri()
            val options = CropImageContractOptions(
                uri = imageUri,
                cropImageOptions = CropImageOptions(
                    customOutputUri = outputUri,
                    imageSourceIncludeGallery = false,
                    imageSourceIncludeCamera = false,
                    allowRotation = true,
                    allowFlipping = true,
                    fixAspectRatio = false
                )
            )
            imageCropLauncher.launch(options)
        }
    }

    LaunchedEffect(imagePath) {
        viewModel.initImagePath(imagePath)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chỉnh sửa & Thêm Ghi chú") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    // SAVE BUTTON
                    IconButton(
                        onClick = {
                            viewModel.savePhoto(
                                onSuccess = {
                                    Toast.makeText(context, "Đã lưu vào nhật ký!", Toast.LENGTH_SHORT).show()
                                    onSaveSuccess()
                                },
                                onError = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        enabled = !editState.isSaving
                    ) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = "Lưu ảnh",
                            modifier = Modifier.size(28.dp)
                        )
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black),
            ) {
                // PHOTO VIEW
                currentDisplayImagePath?.let { path ->
                    Image(
                        painter = rememberAsyncImagePainter(model = File(path)),
                        contentDescription = "Ảnh cần chỉnh sửa",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            // OVERFLOW TOOLBAR
            EditToolbar(selectedTool = selectedTool) { tool ->
                if (tool == EditTool.CROP) {
                    currentBaseImagePath?.let { path ->
                        cropTriggerPath = path
                    }
                } else {
                    selectedTool = if (selectedTool == tool) EditTool.NONE else tool
                }
            }
            AnimatedVisibility(
                visible = selectedTool == EditTool.FILTER || selectedTool == EditTool.NOTE,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (selectedTool) {
                        EditTool.FILTER -> {
                            FilterSelector(
                                currentFilter = editState.filter,
                                onFilterSelected = viewModel::applyFilter
                            )
                        }
                        EditTool.NOTE -> {
                            NoteAndEmojiInput(
                                editState = editState,
                                onNoteChange = viewModel::updateNote,
                                onEmojiIconClick = { showEmojiDialog = true }
                            )
                        }
                        EditTool.CROP, EditTool.NONE -> {
                        }
                    }
                }
            }
            if (showEmojiDialog) {
                EmojiPicker(
                    currentEmoji = editState.emoji.ifEmpty { null },
                    onDismiss = { showEmojiDialog = false },
                    onSelect = { selectedEmoji ->
                        viewModel.updateEmoji(selectedEmoji ?: "") // Lưu String? thành String
                        showEmojiDialog = false
                    }
                )
            }
        }

    }
}
@Composable
fun NoteAndEmojiInput(
    editState: EditState,
    onNoteChange: (String) -> Unit,
    onEmojiIconClick: () -> Unit
) {
    OutlinedTextField(
        value = editState.note,
        onValueChange = onNoteChange,
        label = { Text("Ghi chú tâm trạng") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            Text(editState.emoji,
                 style = LocalTextStyle.current.copy(fontSize = 28.sp),
                 modifier = Modifier.padding(end = 8.dp).clickable {
                     onEmojiIconClick()
                 }
            )
        }
    )
}

@Composable
fun EditToolbar(
    selectedTool: EditTool,
    onToolSelected: (EditTool) -> Unit
) {
    Row(
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolButton(
            icon = Icons.Default.Tune,
            label = "Bộ lọc",
            isSelected = selectedTool == EditTool.FILTER,
            onClick = { onToolSelected(EditTool.FILTER) }
        )
        ToolButton(
            icon = Icons.Default.CropRotate,
            label = "Cắt/Xoay",
            isSelected = selectedTool == EditTool.CROP,
            onClick = { onToolSelected(EditTool.CROP) }
        )
        ToolButton(
            icon = Icons.Default.Edit,
            label = "Ghi chú",
            isSelected = selectedTool == EditTool.NOTE,
            onClick = { onToolSelected(EditTool.NOTE) }
        )
    }
}

@Composable
fun ToolButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(horizontal = 12.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.size(32.dp)
        )
        Text(
            label,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}

@Composable
fun FilterSelector(
    currentFilter: String,
    onFilterSelected: (String) -> Unit
) {

    val filters = listOf("None", "B&W", "Sepia", "Vintage", "Warm", "Cool")
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            AssistChip(
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (filter == currentFilter) MaterialTheme.colorScheme.primary else Color.LightGray,
                    labelColor = if (filter == currentFilter) Color.White else Color.Black
                ),
                border = AssistChipDefaults.assistChipBorder(borderColor = Color.Transparent)
            )
        }
    }
}

enum class EditTool {
    FILTER, CROP, NOTE, NONE
}
