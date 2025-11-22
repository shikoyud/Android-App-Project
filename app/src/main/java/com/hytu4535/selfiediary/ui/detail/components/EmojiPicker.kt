package com.hytu4535.selfiediary.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun EmojiPicker(
    currentEmoji: String?,
    onDismiss: () -> Unit,
    onSelect: (String?) -> Unit
) {
    val emojiCategories = remember {
        mapOf(
            "Cảm xúc" to listOf(
                "😊", "😄", "😁", "😃", "😆", "😅", "🤣", "😂",
                "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗",
                "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨",
                "🧐", "🤓", "😎", "🤩", "🥳", "😏", "😒", "😞",
                "😔", "😟", "😕", "🙁", "☹️", "😣", "😖", "😫",
                "😩", "🥺", "😢", "😭", "😤", "😠", "😡", "🤬"
            ),
            "Hoạt động" to listOf(
                "💪", "👍", "👎", "👌", "✌️", "🤞", "🤟", "🤘",
                "🤙", "👏", "🙌", "👐", "🤲", "🤝", "🙏", "✍️"
            ),
            "Vật phẩm" to listOf(
                "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍",
                "💔", "❣️", "💕", "💞", "💓", "💗", "💖", "💘",
                "💝", "🎂", "🎉", "🎊", "🎈", "🎁", "🏆", "⭐"
            ),
            "Thời tiết" to listOf(
                "☀️", "🌤️", "⛅", "🌥️", "☁️", "🌦️", "🌧️", "⛈️",
                "🌩️", "🌨️", "❄️", "☃️", "⛄", "🌬️", "💨", "🌪️",
                "🌈", "⚡", "🔥", "💧", "🌊", "🌙", "⭐", "✨"
            )
        )
    }

    var selectedCategory by remember { mutableStateOf("Cảm xúc") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chọn cảm xúc",
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextButton(onClick = { onSelect(null) }) {
                        Text("Xóa")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = emojiCategories.keys.indexOf(selectedCategory),
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 0.dp
                ) {
                    emojiCategories.keys.forEach { category ->
                        Tab(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            text = { Text(category) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Emoji Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(emojiCategories[selectedCategory] ?: emptyList()) { emoji ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(
                                    if (emoji == currentEmoji)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                                .clickable {
                                    onSelect(emoji)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Đóng")
                }
            }
        }
    }
}

