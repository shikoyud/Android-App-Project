package com.hytu4535.selfiediary.ui.edit

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
    private val saveSelfieUseCase: SaveSelfieUseCase
) : ViewModel() {

    private val _imagePath = MutableStateFlow<String?>(null)
    val imagePath: StateFlow<String?> = _imagePath.asStateFlow()

    private val _editState = MutableStateFlow(EditState())
    val editState: StateFlow<EditState> = _editState.asStateFlow()

    fun initImagePath(path: String) {
        if (_imagePath.value == null) {
            _imagePath.value = path
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

    fun applyFilter(filterName: String) {
        // TODO: Image processing
        _editState.update { it.copy(filter = filterName) }
    }

    fun rotatePhoto() {
        //TODO: Rotate photo
    }

    fun savePhoto(
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        val originalPath = _imagePath.value ?: run {
            onError("Không tìm thấy đường dẫn ảnh.")
            return
        }

        _editState.update { it.copy(isSaving = true) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val state = _editState.value
                val isEdited = state.filter != "None"
                val finalImagePath = originalPath

                val cal = Calendar.getInstance()

                val entry = SelfieEntry(
                    filePath = originalPath,
                    editedFilePath = if (isEdited) finalImagePath else null,
                    timestamp = System.currentTimeMillis(),
                    note = state.note,
                    emoji = state.emoji,
                    isEdited = isEdited,
                    dayOfMonth = cal.get(Calendar.DAY_OF_MONTH),
                    monthOfYear = cal.get(Calendar.MONTH) + 1
                )

                saveSelfieUseCase.invoke(entry)

                withContext(Dispatchers.Main) {
                    _editState.update { it.copy(isSaving = true) }
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
    val emoji: String = "😀",
    val filter: String = "None",
    val rotationDegrees: Float = 0f,
    val cropRect: android.graphics.RectF? = null,
    val isSaving: Boolean = false
)