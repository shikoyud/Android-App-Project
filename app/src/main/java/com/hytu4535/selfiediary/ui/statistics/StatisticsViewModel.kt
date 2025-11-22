package com.hytu4535.selfiediary.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.usecase.GetStatisticsUseCase
import com.hytu4535.selfiediary.domain.usecase.SelfieStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {

    private val _statistics = MutableStateFlow(SelfieStatistics())
    val statistics: StateFlow<SelfieStatistics> = _statistics.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadStatistics()
    }

    fun refresh() {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val stats = getStatisticsUseCase.execute()
                _statistics.value = stats
            } catch (e: Exception) {
                // Handle error
                println("Error loading statistics: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

