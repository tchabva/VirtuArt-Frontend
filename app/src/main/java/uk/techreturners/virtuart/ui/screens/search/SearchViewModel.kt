package uk.techreturners.virtuart.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.repository.ArtworksRepository
import uk.techreturners.virtuart.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val artworksRepository: ArtworksRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    //TODO fix pagination bug

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State.Search(
            source = authRepository.source.value
        )
    )
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    fun onBasicSearch() {
        viewModelScope.launch {
            val cState = state.value as State.Search
            _state.value = (state.value as State.Search).copy(isSearching = true)

            // Copy the source and page limit from the state
            val searchRequest = cState.basicQuery.copy(
                source = cState.source,
                pageSize = cState.pageSize
            )

            if (!searchRequest.query.isNullOrBlank()) {
                _state.value = cState.copy(isSearching = true)
                searchBasicQuery(searchRequest)
                Log.i(TAG, "Basic Search Query")
            } else {
                emitEvent(
                    Event.EmptySearchQuery
                )
            }
        }
    }

    private suspend fun searchBasicQuery(query: BasicSearchQuery) {
        when (val networkResponse = artworksRepository.basicApiSearch(query)) {
            is NetworkResponse.Exception -> {
                _state.value = State.NetworkError(
                    errorMessage = networkResponse.exception.message ?: "Unknown Error"
                )
                Log.e(TAG, "Network Error: ${networkResponse.exception.message}")
            }

            is NetworkResponse.Failed -> {
                _state.value = State.Error(
                    responseCode = networkResponse.code,
                    errorMessage = networkResponse.message ?: "Unknown Error"
                )
                Log.e(TAG, "Error Code:${networkResponse.code}\n${networkResponse.message}")
            }

            is NetworkResponse.Success -> {
                _state.value = (state.value as State.Search).copy(
                    isSearching = false,
                    data = networkResponse.data,
                    advancedSearchQuery = AdvancedSearchRequest()
                )
                Log.i(TAG, "Basic Search Successful:${networkResponse.data.data}")
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
                _state.value =
                    (state.value as State.Search).copy(isSearching = true) // Loading Spinner
                searchAdvancedQuery(searchRequest)
                Log.i(TAG, "Advanced Elastic Search Query:\n$searchRequest")
            } else {
                emitEvent(
                    Event.EmptySearchQuery
                )
            }
        }
    }

    private suspend fun searchAdvancedQuery(query: AdvancedSearchRequest) {
        when (val networkResponse = artworksRepository.advancedApiSearch(query)) {
            is NetworkResponse.Exception -> {
                _state.value = State.NetworkError(
                    errorMessage = networkResponse.exception.message ?: "Unknown Error"
                )
                Log.e(TAG, "Network Error: ${networkResponse.exception.message}")
            }

            is NetworkResponse.Failed -> {
                _state.value = State.Error(
                    responseCode = networkResponse.code,
                    errorMessage = networkResponse.message ?: "Unknown Error"
                )
                Log.e(TAG, "Error Code:${networkResponse.code}\n${networkResponse.message}")
            }

            is NetworkResponse.Success -> {
                _state.value = (state.value as State.Search).copy(
                    isSearching = false,
                    data = networkResponse.data,
                    basicQuery = BasicSearchQuery() // TODO consider persisting the text onNext/Prev
                )
                Log.i(TAG, "Advanced Search Successful:${networkResponse.data.data}")
            }
        }
    }

    // Update Basic Search
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

    // Toggles the view of the Advanced Search
    fun toggleAdvancedSearch() {
        _state.value = (state.value as State.Search).copy(
            showAdvancedSearch = !(state.value as State.Search).showAdvancedSearch
        )
        Log.i(
            TAG,
            "Advanced search view toggled: ${(state.value as State.Search).showAdvancedSearch}"
        )
    }

    // Update Advanced Search
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

    // Pagination
    fun onPreviousClick() {
        val cState = state.value
        if (cState is State.Search) {
            if (cState.data != null && cState.data.hasPrevious) {
                val nextPage = cState.data.currentPage - 1
                if (!cState.basicQuery.query.isNullOrBlank()) {
                    _state.value = cState.copy(
                        basicQuery = cState.basicQuery.copy(
                            currentPage = nextPage
                        )
                    )
                    Log.i(TAG, "onPreviousClicked for Basic Search in Pagination")
                    onBasicSearch()
                } else {
                    _state.value = cState.copy(
                        advancedSearchQuery = cState.advancedSearchQuery.copy(
                            currentPage = nextPage
                        )
                    )
                    Log.i(TAG, "onPreviousClicked for Advanced Search in Pagination")
                    onAdvancedSearchFormSubmit()
                }
            }
        }
    }

    fun onNextClick() {
        val cState = state.value
        if (cState is State.Search) {
            if (cState.data != null && cState.data.hasNext) {
                val nextPage = cState.data.currentPage + 1
                if (!cState.basicQuery.query.isNullOrBlank()) {
                    _state.value = cState.copy(
                        basicQuery = cState.basicQuery.copy(
                            currentPage = nextPage
                        )
                    )
                    Log.i(TAG, "onNextClicked for Basic Search in Pagination")
                    onBasicSearch()
                } else {
                    _state.value = cState.copy(
                        advancedSearchQuery = cState.advancedSearchQuery.copy(
                            currentPage = nextPage
                        )
                    )
                    Log.i(TAG, "onNextClicked for Advanced Search in Pagination")
                    onAdvancedSearchFormSubmit()
                }
            }
        }
    }

    // Toggle the Api Source Dialog
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

    // Toggle the PageSizeDialog
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
            Log.i(
                TAG,
                "Updated the Page Size: ${(state.value as State.Search).pageSize}"
            )
        } else {
            Log.i(TAG, "Page Size not updated")
        }
    }

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    sealed interface State {
        data class Search(
            val data: PaginatedArtworkResults? = null,
            val basicQuery: BasicSearchQuery = BasicSearchQuery(),
            val advancedSearchQuery: AdvancedSearchRequest = AdvancedSearchRequest(),
            val isSearching: Boolean = false,
            val showAdvancedSearch: Boolean = false,
            val showSearchRelevance: Boolean = false,
            val showSortOrder: Boolean = false,
            val showApiSource: Boolean = false,
            val showPageSize: Boolean = false,
            val isUserSignedIn: Boolean = false,
            val pageSize: Int = 20,
            val source: String,
            val showBasicSearch: Boolean = true, // TODO
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
        data class ClickedOnArtwork(val source: String, val artworkId: String) : Event
        data object EmptySearchQuery : Event
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}