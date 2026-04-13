package uk.techreturners.virtuart.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.paging.SearchPagingParams
import uk.techreturners.virtuart.data.repository.SearchRepository
import uk.techreturners.virtuart.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private data class SearchSession(
        val params: SearchPagingParams,
        val refreshId: Long,
    )

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State.Search(
            source = authRepository.source.value
        )
    )

    val state: StateFlow<State> = _state

    private val _searchMetadata = MutableStateFlow<PaginatedArtworkResults?>(null)
    val searchMetadata: StateFlow<PaginatedArtworkResults?> = _searchMetadata.asStateFlow()

    private val _searchSession = MutableStateFlow<SearchSession?>(null)

    val hasActiveSearch: StateFlow<Boolean> = _searchSession
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val searchResults: Flow<PagingData<ArtworkResult>> = _searchSession
        .flatMapLatest { session ->
            if (session == null) {
                flowOf(PagingData.empty())
            } else {
                searchRepository.searchPaged(session.params) { meta ->
                    _searchMetadata.value = meta
                }
            }
        }
        .cachedIn(viewModelScope)

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    fun onBasicSearch() {
        viewModelScope.launch {
            val cState = state.value as State.Search
            val searchRequest = cState.basicQuery.copy(
                source = cState.source,
                pageSize = cState.pageSize
            )

            if (!searchRequest.query.isNullOrBlank()) {
                _searchMetadata.value = null
                _searchSession.value = SearchSession(
                    params = SearchPagingParams.Basic(
                        query = searchRequest.query.trim(),
                        source = cState.source,
                        pageSize = cState.pageSize,
                    ),
                    refreshId = System.nanoTime(),
                )
                Log.i(TAG, "Basic Search Query")
            } else {
                emitEvent(
                    Event.EmptySearchQuery
                )
            }
        }
    }

    fun onAdvancedSearchFormSubmit() {
        viewModelScope.launch {
            val cState = state.value as State.Search
            val searchRequest = cState.advancedSearchQuery.copy(
                source = cState.source,
                pageSize = cState.pageSize
            )

            if (
                !searchRequest.title.isNullOrBlank() || !searchRequest.artist.isNullOrBlank() ||
                !searchRequest.medium.isNullOrBlank() || !searchRequest.department.isNullOrBlank()
            ) {
                _state.value = (state.value as State.Search).copy(showAdvancedSearch = false)
                _searchMetadata.value = null
                _searchSession.value = SearchSession(
                    params = SearchPagingParams.Advanced(
                        request = searchRequest,
                    ),
                    refreshId = System.nanoTime(),
                )
                Log.i(TAG, "Advanced Elastic Search Query:\n$searchRequest")
            } else {
                emitEvent(
                    Event.EmptySearchQuery
                )
            }
        }
    }

    fun updateBasicSearch(newQuery: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                basicQuery = BasicSearchQuery(query = newQuery)
            )
        }
        Log.i(TAG, "Basic search query updated: $newQuery")
    }

    fun clearBasicSearch() {
        _state.value = (state.value as State.Search).copy(
            basicQuery = BasicSearchQuery()
        )
        Log.i(TAG, "Cleared Basic Search TextField")
    }

    fun toggleAdvancedSearch() {
        _state.value = (state.value as State.Search).copy(
            showAdvancedSearch = !(state.value as State.Search).showAdvancedSearch
        )
        Log.i(
            TAG,
            "Advanced search view toggled: ${(state.value as State.Search).showAdvancedSearch}"
        )
    }

    fun updateAdvancedSearchTitle(newTitle: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(
                    title = newTitle,
                    currentPage = 1
                )
            )
        }
        Log.i(TAG, "Advanced search title updated: $newTitle")
    }

    fun updateAdvancedSearchArtist(newArtist: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(artist = newArtist)
            )
        }
        Log.i(TAG, "Advanced search artist updated: $newArtist")
    }

    fun updateAdvancedSearchMedium(newMedium: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(medium = newMedium)
            )
        }
        Log.i(TAG, "Advanced search medium updated: $newMedium")
    }

    fun updateAdvancedSearchDepartment(newDepartment: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(
                    department = newDepartment,
                    currentPage = 1
                )
            )
        }
        Log.i(TAG, "Advanced search department updated: $newDepartment")
    }

    fun updateAdvancedSearchSortBy(newSortBy: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(
                    sortBy = newSortBy,
                    currentPage = 1
                )
            )
        }
        Log.i(TAG, "Advanced search sort by updated: $newSortBy")
    }

    fun updateAdvancedSearchSortOrder(newSortOrder: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            if (newSortOrder.lowercase() == "asc") {
                _state.value = currentState.copy(
                    advancedSearchQuery = currentState.advancedSearchQuery.copy(
                        sortOrder = "asc",
                        currentPage = 1
                    )
                )
            } else {
                _state.value = currentState.copy(
                    advancedSearchQuery = currentState.advancedSearchQuery.copy(
                        sortOrder = "desc",
                        currentPage = 1
                    )
                )
            }
        }
        Log.i(TAG, "Advanced search sort order updated: $newSortOrder")
    }

    fun onAdvancedSearchFormClear() {
        _state.value = (state.value as State.Search).copy(
            advancedSearchQuery = AdvancedSearchRequest()
        )
        Log.i(TAG, "Cleared Advanced Search TextFields")
    }

    fun toggleShowApiSourceDialog() {
        _state.value = (state.value as State.Search).copy(
            showApiSource = !(state.value as State.Search).showApiSource
        )
        Log.i(
            TAG,
            "Toggle the showApiSource: ${(state.value as State.Search).showApiSource}"
        )
    }

    fun updateApiSource(newSource: String) {
        val cState = state.value as State.Search
        if (cState.source != newSource) {
            authRepository.updateSource(newSource)
            _searchSession.value = null
            _searchMetadata.value = null
            _state.value = State.Search(
                source = authRepository.source.value
            )
            Log.i(
                TAG,
                "Updated the Api Source: ${(state.value as State.Search).source}"
            )
        } else {
            Log.i(TAG, "Api source not updated")
        }
    }

    fun toggleShowPageSizeDialog() {
        _state.value = (state.value as State.Search).copy(
            showPageSize = !(state.value as State.Search).showPageSize
        )
        Log.i(
            TAG,
            "Toggle the showPageLimit dialog: ${(state.value as State.Search).showPageSize}"
        )
    }

    fun updatePageSize(newPageSize: Int) {
        val cState = state.value as State.Search
        if (cState.pageSize != newPageSize) {
            _state.value = cState.copy(
                pageSize = newPageSize
            )
            val currentSession = _searchSession.value
            if (currentSession != null) {
                _searchMetadata.value = null
                val newParams = when (val p = currentSession.params) {
                    is SearchPagingParams.Basic -> p.copy(pageSize = newPageSize)
                    is SearchPagingParams.Advanced -> SearchPagingParams.Advanced(
                        p.request.copy(pageSize = newPageSize)
                    )
                }
                _searchSession.value = SearchSession(
                    params = newParams,
                    refreshId = System.nanoTime(),
                )
            }
            Log.i(
                TAG,
                "Updated the Page Size: ${(state.value as State.Search).pageSize}"
            )
        } else {
            Log.i(TAG, "Page Size not updated")
        }
    }

    fun onReturnToSearchButtonClicked() {
        _searchSession.value = null
        _searchMetadata.value = null
        _state.value = State.Search(
            source = authRepository.source.value
        )
        Log.i(TAG, "Return to Search Button clicked")
    }

    sealed interface State {
        data class Search(
            val basicQuery: BasicSearchQuery = BasicSearchQuery(),
            val advancedSearchQuery: AdvancedSearchRequest = AdvancedSearchRequest(),
            val showAdvancedSearch: Boolean = false,
            val showSearchRelevance: Boolean = false,
            val showSortOrder: Boolean = false,
            val showApiSource: Boolean = false,
            val showPageSize: Boolean = false,
            val isUserSignedIn: Boolean = false,
            val pageSize: Int = 20,
            val source: String,
            val showBasicSearch: Boolean = true,
        ) : State
    }

    sealed interface Event {
        data class ClickedOnArtwork(val source: String, val artworkId: String) : Event
        data object EmptySearchQuery : Event
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}
