package com.hytu4535.selfiediary.ui.edit

import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.domain.usecase.SaveSelfieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val saveSelfieUseCase: SaveSelfieUseCase,
    val imageProcessor: ImageProcessor
) : ViewModel() {

    private val _originalImagePath = MutableStateFlow<String?>(null)
    private val _baseImagePath = MutableStateFlow<String?>(null)
    val baseImagePath: StateFlow<String?> = _baseImagePath.asStateFlow()
    private val _displayImagePath = MutableStateFlow<String?>(null)
    val displayImagePath: StateFlow<String?> = _displayImagePath.asStateFlow()
    private val _editState = MutableStateFlow(EditState())
    val editState: StateFlow<EditState> = _editState.asStateFlow()

    fun initImagePath(path: String) {
        if (_originalImagePath.value == null) {
            _originalImagePath.value = path
            _baseImagePath.value = path
            _displayImagePath.value = path
        }
    }

    fun updateNote(note: String) {
        if (note.length <= 50) {
            _editState.update { it.copy(note = note) }
        }
    }

    fun updateEmoji(emoji: String) {
        _editState.update { it.copy(emoji = emoji) }
    }

    fun applyRotation(newPath: String) {
        val currentFilter = _editState.value.filter
        _baseImagePath.value = newPath
        if (currentFilter == "None") {
            _displayImagePath.value = newPath
        } else {
            applyFilter(currentFilter, true)
        }
    }

    fun applyFilter(filterName: String, forceApply: Boolean = false) {
        val currentBase = _baseImagePath.value
        if (currentBase == null) return
        if (filterName == _editState.value.filter && !forceApply) return

        _editState.update { it.copy(filter = filterName) }

        val newDisplayPath = imageProcessor.applyFilterAndSaveTemp(currentBase, filterName)
        _displayImagePath.value = newDisplayPath
    }

    fun savePhoto(
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        val pathToBeSaved = _displayImagePath.value ?: run {
            onError("Không tìm thấy đường dẫn ảnh.")
            return
        }

        _editState.update { it.copy(isSaving = true) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val state = _editState.value
                val finalSavedPath = imageProcessor.processAndSave(imagePath = pathToBeSaved)
                if (finalSavedPath == null) {
                    throw Exception("Không thể lưu ảnh cuối cùng.")
                }
                val isEdited = (_displayImagePath.value != _originalImagePath.value) || (state.filter != "None")

                val cal = Calendar.getInstance()

                val entry = SelfieEntry(
                    filePath = finalSavedPath,
                    editedFilePath = if (isEdited) finalSavedPath else null,
                    timestamp = System.currentTimeMillis(),
                    note = state.note,
                    emoji = state.emoji,
                    isEdited = isEdited,
                    dayOfMonth = cal.get(Calendar.DAY_OF_MONTH),
                    monthOfYear = cal.get(Calendar.MONTH) + 1
                )

                saveSelfieUseCase.invoke(entry)

                withContext(Dispatchers.Main) {
                    _editState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _editState.update { it.copy(isSaving = false) }
                    onError(e.localizedMessage ?: "Lỗi khi lưu ảnh.")
                }
            }
        }
    }
}

data class EditState(
    val note: String = "",
    val emoji: String = "😊",
    val filter: String = "None",
    val isSaving: Boolean = false
)
