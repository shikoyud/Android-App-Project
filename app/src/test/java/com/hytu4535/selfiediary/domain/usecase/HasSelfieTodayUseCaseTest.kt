package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for HasSelfieTodayUseCase
 * Rất quan trọng cho tính năng Smart Reminder!
 */
class HasSelfieTodayUseCaseTest {

    @Mock
    private lateinit var repository: SelfieRepository

    private lateinit var useCase: HasSelfieTodayUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = HasSelfieTodayUseCase(repository)
    }

    @Test
    fun `invoke should return true when user has selfie today`() = runTest {
        // Given
        `when`(repository.hasSelfieToday()).thenReturn(true)

        // When
        val result = useCase()

        // Then
        assertTrue(result)
        verify(repository).hasSelfieToday()
    }

    @Test
    fun `invoke should return false when user has no selfie today`() = runTest {
        // Given
        `when`(repository.hasSelfieToday()).thenReturn(false)

        // When
        val result = useCase()

        // Then
        assertFalse(result)
        verify(repository).hasSelfieToday()
    }

    @Test
    fun `invoke should return false when repository throws exception`() = runTest {
        // Given
        `when`(repository.hasSelfieToday()).thenThrow(RuntimeException("Error"))

        // When
        val result = useCase()

        // Then
        assertFalse(result) // Should not crash, just return false
    }
}

