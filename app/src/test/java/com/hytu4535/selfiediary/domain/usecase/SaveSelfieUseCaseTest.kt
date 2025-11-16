package com.hytu4535.selfiediary.domain.usecase

import com.hytu4535.selfiediary.data.repository.SelfieRepository
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for SaveSelfieUseCase
 *
 * Hướng dẫn chạy test:
 * 1. Trong Android Studio, click chuột phải vào file này
 * 2. Chọn "Run 'SaveSelfieUseCaseTest'"
 *
 * Dependencies cần thêm vào build.gradle.kts:
 * testImplementation("junit:junit:4.13.2")
 * testImplementation("org.mockito:mockito-core:5.3.1")
 * testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
 * testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
 */
class SaveSelfieUseCaseTest {

    @Mock
    private lateinit var repository: SelfieRepository

    private lateinit var useCase: SaveSelfieUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = SaveSelfieUseCase(repository)
    }

    @Test
    fun `invoke should save selfie successfully`() = runTest {
        // Given
        val entry = SelfieEntry(
            filePath = "/test/path.jpg",
            timestamp = System.currentTimeMillis(),
            note = "Test note"
        )
        val expectedId = 1L
        `when`(repository.insertSelfie(entry)).thenReturn(expectedId)

        // When
        val result = useCase(entry)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedId, result.getOrNull())
        verify(repository).insertSelfie(entry)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        val entry = SelfieEntry(
            filePath = "/test/path.jpg",
            timestamp = System.currentTimeMillis()
        )
        val exception = RuntimeException("Database error")
        `when`(repository.insertSelfie(entry)).thenThrow(exception)

        // When
        val result = useCase(entry)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}

