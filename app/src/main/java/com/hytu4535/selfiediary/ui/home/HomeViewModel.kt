package com.hytu4535.selfiediary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.model.OnThisDayEntry
import com.hytu4535.selfiediary.domain.model.SelfieEntry
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
    private val getOnThisDayUseCase: GetOnThisDayUseCase
) : ViewModel() {

    private val _selfies = MutableStateFlow<List<SelfieEntry>>(emptyList())
    val selfies: StateFlow<List<SelfieEntry>> = _selfies.asStateFlow()

    private val _onThisDay = MutableStateFlow<OnThisDayEntry?>(null)
    val onThisDay: StateFlow<OnThisDayEntry?> = _onThisDay.asStateFlow()

    init {
        loadSelfies()
        loadOnThisDay()
    }

    private fun loadSelfies() {
        viewModelScope.launch {
            getAllSelfiesUseCase().collect { selfieList ->
                _selfies.value = selfieList
            }
        }
    }

    private fun loadOnThisDay() {
        viewModelScope.launch {
            _onThisDay.value = getOnThisDayUseCase()
        }
    }
}

