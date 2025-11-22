package com.hytu4535.selfiediary.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.domain.usecase.DeleteSelfiesUseCase
import com.hytu4535.selfiediary.domain.usecase.GetAllSelfiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getAllSelfiesUseCase: GetAllSelfiesUseCase,
    private val deleteSelfiesUseCase: DeleteSelfiesUseCase
) : ViewModel() {

    private val _selfies = MutableStateFlow<List<SelfieEntry>>(emptyList())
    val selfies: StateFlow<List<SelfieEntry>> = _selfies.asStateFlow()

    private val _selectedItems = MutableStateFlow<Set<Long>>(emptySet())
    val selectedItems: StateFlow<Set<Long>> = _selectedItems.asStateFlow()

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

    init {
        loadSelfies()
    }

    private fun loadSelfies() {
        viewModelScope.launch {
            getAllSelfiesUseCase().collect { selfieList ->
                _selfies.value = selfieList
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

