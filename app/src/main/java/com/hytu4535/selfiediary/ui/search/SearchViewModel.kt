package com.hytu4535.selfiediary.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hytu4535.selfiediary.domain.model.SelfieEntry
import com.hytu4535.selfiediary.domain.usecase.SearchSelfiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchSelfiesUseCase: SearchSelfiesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow(SearchType.ALL)
    val searchType: StateFlow<SearchType> = _searchType.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SelfieEntry>>(emptyList())
    val searchResults: StateFlow<List<SelfieEntry>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        // Observe search query and perform search with debounce
        viewModelScope.launch {
            searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .combine(searchType) { query, type -> query to type }
                .collectLatest { (query, type) ->
                    if (query.isNotEmpty()) {
                        performSearch(query, type)
                    } else {
                        _searchResults.value = emptyList()
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSearchType(type: SearchType) {
        _searchType.value = type
        // Re-trigger search with new type
        if (_searchQuery.value.isNotEmpty()) {
            performSearch(_searchQuery.value, type)
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    private fun performSearch(query: String, type: SearchType) {
        viewModelScope.launch {
            _isSearching.value = true
            try {
                val flow = when (type) {
                    SearchType.ALL -> searchSelfiesUseCase.searchAll(query)
                    SearchType.NOTE -> searchSelfiesUseCase.searchByNote(query)
                    SearchType.EMOJI -> searchSelfiesUseCase.searchByEmoji(query)
                    SearchType.TAGS -> searchSelfiesUseCase.searchByTag(query)
                }

                flow.collect { results ->
                    _searchResults.value = results
                    _isSearching.value = false
                }
            } catch (e: Exception) {
                println("Error searching: ${e.message}")
                _isSearching.value = false
                _searchResults.value = emptyList()
            }
        }
    }
}

