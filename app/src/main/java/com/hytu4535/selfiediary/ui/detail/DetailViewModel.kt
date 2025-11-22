package com.hytu4535.selfiediary.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.domain.usecase.DeleteSelfiesUseCase
import com.hytu4535.selfiediary.domain.usecase.GetAllSelfiesUseCase
import com.hytu4535.selfiediary.domain.usecase.UpdateNoteAndEmojiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getAllSelfiesUseCase: GetAllSelfiesUseCase,
    private val deleteSelfiesUseCase: DeleteSelfiesUseCase,
    private val updateNoteAndEmojiUseCase: UpdateNoteAndEmojiUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selfieId: Long = savedStateHandle.get<Long>("selfieId") ?: 0L

    private val _currentSelfie = MutableStateFlow<SelfieEntry?>(null)
    val currentSelfie: StateFlow<SelfieEntry?> = _currentSelfie.asStateFlow()

    private val _allSelfies = MutableStateFlow<List<SelfieEntry>>(emptyList())
    val allSelfies: StateFlow<List<SelfieEntry>> = _allSelfies.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _showNoteEditor = MutableStateFlow(false)
    val showNoteEditor: StateFlow<Boolean> = _showNoteEditor.asStateFlow()

    private val _showEmojiPicker = MutableStateFlow(false)
    val showEmojiPicker: StateFlow<Boolean> = _showEmojiPicker.asStateFlow()

    init {
        loadSelfies()
    }

    private fun loadSelfies() {
        viewModelScope.launch {
            getAllSelfiesUseCase().collect { selfieList ->
                _allSelfies.value = selfieList
                val index = selfieList.indexOfFirst { it.id == selfieId }
                if (index >= 0) {
                    _currentIndex.value = index
                    _currentSelfie.value = selfieList[index]
                }
            }
        }
    }

    fun navigateToNext() {
        val selfies = _allSelfies.value
        if (selfies.isEmpty()) return

        val newIndex = (_currentIndex.value + 1) % selfies.size
        _currentIndex.value = newIndex
        _currentSelfie.value = selfies[newIndex]
    }

    fun navigateToPrevious() {
        val selfies = _allSelfies.value
        if (selfies.isEmpty()) return

        val newIndex = if (_currentIndex.value > 0) {
            _currentIndex.value - 1
        } else {
            selfies.size - 1
        }
        _currentIndex.value = newIndex
        _currentSelfie.value = selfies[newIndex]
    }

    fun showNoteEditor() {
        _showNoteEditor.value = true
    }

    fun hideNoteEditor() {
        _showNoteEditor.value = false
    }

    fun showEmojiPicker() {
        _showEmojiPicker.value = true
    }

    fun hideEmojiPicker() {
        _showEmojiPicker.value = false
    }

    fun updateNote(note: String) {
        viewModelScope.launch {
            _currentSelfie.value?.let { selfie ->
                updateNoteAndEmojiUseCase.execute(selfie.id, note, selfie.emoji)
                _currentSelfie.value = selfie.copy(note = note)
            }
        }
    }

    fun updateEmoji(emoji: String?) {
        viewModelScope.launch {
            _currentSelfie.value?.let { selfie ->
                updateNoteAndEmojiUseCase.execute(selfie.id, selfie.note, emoji)
                _currentSelfie.value = selfie.copy(emoji = emoji)
            }
        }
    }

    fun deleteCurrent(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _currentSelfie.value?.let { selfie ->
                deleteSelfiesUseCase(listOf(selfie.id))
                onDeleted()
            }
        }
    }
}

