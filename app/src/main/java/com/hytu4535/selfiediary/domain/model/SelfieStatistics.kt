package com.hytu4535.selfiediary.domain.model

/**
 * Domain model for selfie statistics
 */
data class SelfieStatistics(
    val totalCount: Int = 0,
    val editedCount: Int = 0,
    val syncedCount: Int = 0,
    val monthlyCount: Map<Int, Int> = emptyMap(), // Month -> Count
    val mostUsedEmojis: List<EmojiStat> = emptyList()
)

/**
 * Statistics for emoji usage
 */
data class EmojiStat(
    val emoji: String,
    val count: Int
)

