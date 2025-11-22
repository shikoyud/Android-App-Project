package com.hytu4535.selfiediary.ui.edit

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
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
    val currentImagePath by viewModel.imagePath.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
                            tint = Color.Black,
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
                    .weight(0.6f)
                    .background(Color.DarkGray)
            ) {
                currentImagePath?.let { path ->
                    // SELFIE VIEW
                    Image(
                        painter = rememberAsyncImagePainter(model = File(path)),
                        contentDescription = "Ảnh cần chỉnh sửa",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            // 👈 Áp dụng hiệu ứng xoay từ ViewModel cho UI
                            .graphicsLayer { rotationZ = editState.rotationDegrees }
                    )
                }

                // ROTATE PHOTO
                IconButton(
                    onClick = viewModel::rotatePhoto,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.RotateRight,
                        contentDescription = "Xoay ảnh",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // FILTER AND NOTES
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(16.dp)
            ) {
                // FILTER
                Text("Bộ lọc", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                FilterSelector(
                    currentFilter = editState.filter,
                    onFilterSelected = viewModel::applyFilter
                )

                Spacer(modifier = Modifier.height(16.dp))

                // NOTES
                OutlinedTextField(
                    value = editState.note,
                    onValueChange = viewModel::updateNote,
                    label = { Text("Ghi chú tâm trạng (Tối đa 50 ký tự)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    trailingIcon = {
                        Text(editState.emoji,
                             style = LocalTextStyle.current.copy(fontSize = 28.sp),
                             modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                )
            }
        }
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