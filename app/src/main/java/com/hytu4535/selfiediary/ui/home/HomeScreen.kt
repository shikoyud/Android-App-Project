package com.hytu4535.selfiediary.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.ui.home.components.FilterIndicator
import com.hytu4535.selfiediary.ui.home.components.OnThisDayCard as OnThisDayCardComponent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToCapture: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToDateFilter: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit,
    navController: NavController? = null,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val selfies by viewModel.selfies.collectAsState()
    val onThisDay by viewModel.onThisDay.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()
    val isSelectionMode by viewModel.isSelectionMode.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isFiltered by viewModel.isFiltered.collectAsState()
    val groupedSelfies = groupSelfiesByDate(selfies)

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Observe date range result from DateFilterScreen
    LaunchedEffect(navController?.currentBackStackEntry) {
        navController?.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Pair<Long, Long>>("date_range")
            ?.let { (start, end) ->
                viewModel.filterByDateRange(start, end)
                // Clear the saved state after consuming
                navController.currentBackStackEntry?.savedStateHandle?.remove<Pair<Long, Long>>("date_range")
            }
    }


    Scaffold(
        topBar = {
            if (isSelectionMode) {
                // Contextual Action Bar
                TopAppBar(
                    title = { Text("Đã chọn: ${selectedItems.size}") },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.clearSelection() }) {
                            Icon(Icons.Default.Close, contentDescription = "Hủy")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.selectAll() }) {
                            Icon(Icons.Default.SelectAll, contentDescription = "Chọn tất cả")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            } else {
                TopAppBar(
                    title = { Text("Nhật ký Selfie", fontWeight = FontWeight.Bold) },
                    actions = {
                        // DEBUG: Test On This Day button
                        IconButton(
                            onClick = { viewModel.createTestPhotoForOnThisDay() }
                        ) {
                            Text("🎂", style = MaterialTheme.typography.titleLarge)
                        }

                        IconButton(onClick = onNavigateToSearch) {
                            Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
                        }
                        IconButton(onClick = onNavigateToDateFilter) {
                            Icon(Icons.Default.FilterList, contentDescription = "Lọc theo ngày")
                        }
                        IconButton(onClick = onNavigateToStatistics) {
                            Icon(Icons.Default.Analytics, contentDescription = "Thống kê")
                        }
                        IconButton(
                            onClick = { viewModel.refresh() },
                            enabled = !isRefreshing
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Làm mới")
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Cài đặt")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = onNavigateToCapture,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Chụp Selfie")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Show empty state only when no filter is active and truly no photos
            if (selfies.isEmpty() && !isRefreshing && !isFiltered) {
                EmptyState(onCaptureClick = onNavigateToCapture)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Filter Indicator
                    if (isFiltered && !isSelectionMode) {
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                            FilterIndicator(
                                onClearFilter = { viewModel.clearFilter() }
                            )
                        }
                    }

                    // On This Day Section
                    if (onThisDay != null && !isSelectionMode && !isFiltered) {
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                            val thisDayEntry = onThisDay // Local variable to avoid smart cast issue
                            if (thisDayEntry != null) {
                                OnThisDayCardComponent(
                                    entry = thisDayEntry,
                                    onImageClick = { onNavigateToDetail(it) }
                                )
                            }
                        }
                    }

                    // Grouped Selfies by Date
                    groupedSelfies.forEach { (date, entries) ->
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                            DateHeader(date = date)
                        }

                        items(entries) { selfie ->
                            SelectableSelfieGridItem(
                                selfie = selfie,
                                isSelected = selectedItems.contains(selfie.id),
                                isSelectionMode = isSelectionMode,
                                onShortClick = {
                                    if (isSelectionMode) {
                                        viewModel.toggleSelection(selfie.id)
                                    } else {
                                        onNavigateToDetail(selfie.id)
                                    }
                                },
                                onLongClick = {
                                    viewModel.toggleSelection(selfie.id)
                                }
                            )
                        }
                    }

                    // Empty filter result message
                    if (isFiltered && selfies.isEmpty()) {
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "😔",
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Không tìm thấy ảnh",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Không có ảnh nào trong khoảng thời gian này",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Xác nhận xóa") },
                text = { Text("Bạn có chắc muốn xóa ${selectedItems.size} ảnh đã chọn? Hành động này không thể hoàn tác.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteSelected()
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
}


@Composable
private fun DateHeader(date: String) {
    Text(
        text = date,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SelectableSelfieGridItem(
    selfie: SelfieEntry,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onShortClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.small)
            .combinedClickable(
                onClick = onShortClick,
                onLongClick = onLongClick
            )
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                } else Modifier
            )
    ) {
        AsyncImage(
            model = selfie.filePath,
            contentDescription = "Selfie ${selfie.timestamp}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Selection overlay
        if (isSelectionMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isSelected) Color.Black.copy(alpha = 0.3f)
                        else Color.Transparent
                    )
            )
        }

        // Emoji overlay if exists
        if (selfie.emoji != null && !isSelectionMode) {
            Text(
                text = selfie.emoji,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Check icon when selected
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Đã chọn",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun EmptyState(onCaptureClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📸",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Chưa có ảnh nào",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bắt đầu ghi lại khoảnh khắc của bạn",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onCaptureClick) {
            Text("Chụp ảnh đầu tiên")
        }
    }
}

private fun groupSelfiesByDate(selfies: List<SelfieEntry>): Map<String, List<SelfieEntry>> {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    return selfies.groupBy { selfie ->
        val selfieDate = Calendar.getInstance().apply {
            timeInMillis = selfie.timestamp
        }

        when {
            isSameDay(selfieDate, today) -> "Hôm nay"
            isSameDay(selfieDate, yesterday) -> "Hôm qua"
            else -> dateFormat.format(Date(selfie.timestamp))
        }
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

