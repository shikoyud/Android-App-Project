package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.EmojiStat
import com.hytu4535.selfiediary.domain.model.SelfieStatistics
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for GetStatisticsUseCase
 */
class GetStatisticsUseCaseTest {

    @Mock
    private lateinit var repository: SelfieRepository

    private lateinit var useCase: GetStatisticsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetStatisticsUseCase(repository)
    }

    @Test
    fun `invoke should return statistics successfully`() = runTest {
        // Given
        val expectedStats = SelfieStatistics(
            totalCount = 100,
            editedCount = 20,
            syncedCount = 50,
            monthlyCount = mapOf(1 to 10, 2 to 15),
            mostUsedEmojis = listOf(
                EmojiStat("😊", 30),
                EmojiStat("😎", 20)
            )
        )
        `when`(repository.getStatistics()).thenReturn(expectedStats)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val stats = result.getOrNull()
        assertEquals(100, stats?.totalCount)
        assertEquals(20, stats?.editedCount)
        assertEquals("😊", stats?.mostUsedEmojis?.firstOrNull()?.emoji)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        `when`(repository.getStatistics()).thenThrow(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
    }
}

