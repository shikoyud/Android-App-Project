package com.hytu4535.selfiediary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.model.OnThisDayEntry
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.domain.usecase.DeleteSelfiesUseCase
import com.hytu4535.selfiediary.domain.usecase.GetAllSelfiesUseCase
import com.hytu4535.selfiediary.domain.usecase.GetOnThisDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllSelfiesUseCase: GetAllSelfiesUseCase,
    private val getOnThisDayUseCase: GetOnThisDayUseCase,
    private val deleteSelfiesUseCase: DeleteSelfiesUseCase
) : ViewModel() {

    private val _selfies = MutableStateFlow<List<SelfieEntry>>(emptyList())
    val selfies: StateFlow<List<SelfieEntry>> = _selfies.asStateFlow()

    private val _onThisDay = MutableStateFlow<OnThisDayEntry?>(null)
    val onThisDay: StateFlow<OnThisDayEntry?> = _onThisDay.asStateFlow()

    private val _selectedItems = MutableStateFlow<Set<Long>>(emptySet())
    val selectedItems: StateFlow<Set<Long>> = _selectedItems.asStateFlow()

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadSelfies()
        loadOnThisDay()
    }

    fun loadSelfies() {
        viewModelScope.launch {
            getAllSelfiesUseCase().collect { selfieList ->
                _selfies.value = selfieList
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadSelfies()
            loadOnThisDay()
            _isRefreshing.value = false
        }
    }

    private fun loadOnThisDay() {
        viewModelScope.launch {
            getOnThisDayUseCase.getMostRecent().collect { entry ->
                _onThisDay.value = entry
            }
        }
    }

    fun toggleSelection(id: Long) {
        val current = _selectedItems.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _selectedItems.value = current
        _isSelectionMode.value = current.isNotEmpty()
    }

    fun clearSelection() {
        _selectedItems.value = emptySet()
        _isSelectionMode.value = false
    }

    fun selectAll() {
        _selectedItems.value = _selfies.value.map { it.id }.toSet()
        _isSelectionMode.value = true
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val idsToDelete = _selectedItems.value.toList()
            deleteSelfiesUseCase(idsToDelete)
            clearSelection()
        }
    }
}

