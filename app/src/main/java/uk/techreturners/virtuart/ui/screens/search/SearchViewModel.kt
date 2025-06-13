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
import uk.techreturners.virtuart.data.model.AicApiElasticSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.repository.ArtworksRepository
import uk.techreturners.virtuart.domain.model.AicAdvancedSearchQuery
import uk.techreturners.virtuart.domain.model.BasicQuery
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val artworksRepository: ArtworksRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Search())
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events


    fun searchArtworks() {
        val cState = (state.value as State.Search).copy()

        viewModelScope.launch {
            if (cState.basicQuery == null && cState.advancedSearchQuery == null) {
                emitEvent(event = Event.EmptySearchQuery)
                Log.i(TAG, "Empty Search Query")
            } else if (cState.basicQuery != null && cState.advancedSearchQuery == null) {
                // TODO
                Log.i(TAG, "Basic Search Query")
            } else if (cState.basicQuery == null && cState.advancedSearchQuery != null) {
//                advancedSearchQuery(cState.advancedSearchQuery)
                // TODO
                Log.i(TAG, "Advanced Search Query")
            }
        }
    }

    private suspend fun basicSearchQuery(basicQuery: BasicQuery) {
        _state.value = (state.value as State.Search).copy(isSearching = true)
        // TODO Implement the basicSearch on the Backend
    }

    fun onAdvancedSearchFormSubmit() {
        _state.value = (state.value as State.Search).copy(showAdvancedSearch = false)
        viewModelScope.launch {
            advancedSearchQuery()
        }
    }

    private suspend fun advancedSearchQuery() {
        val cState = state.value as State.Search
        _state.value = (state.value as State.Search).copy(isSearching = true) // Loading Spinner

        // Must clauses map,
        val mustClauses = mutableListOf<Map<String, Any>>()
        cState.advancedSearchQuery.title?.takeIf { it.isNotBlank() }
            ?.let { mustClauses.add(mapOf("match" to mapOf("title" to it))) }
        cState.advancedSearchQuery.artist?.takeIf { it.isNotBlank() }
            ?.let { mustClauses.add(mapOf("match" to mapOf("artist_title" to it))) }
        cState.advancedSearchQuery.medium?.takeIf { it.isNotBlank() }
            ?.let { mustClauses.add(mapOf("match" to mapOf("medium_display" to it))) }
        cState.advancedSearchQuery.category?.takeIf { it.isNotBlank() }
            ?.let { mustClauses.add(mapOf("match" to mapOf("department_title" to it))) }
        mustClauses.add(mapOf("term" to mapOf("is_public_domain" to true)))

        // Sort clauses
        val sortClauses = mapOf(
            cState.advancedSearchQuery.sortBy to mapOf(
                "order" to cState.advancedSearchQuery.sortOrder
            )
        )

        val elasticSearchQuery = AicApiElasticSearchQuery(
            query = mapOf("bool" to mapOf("must" to mustClauses)),
            sort = listOf(sortClauses),
            size = cState.pageSize,
            page = cState.currentPage
        )

        Log.i(TAG, "Elastic Search Query:\n$elasticSearchQuery")

        when (val networkResponse = artworksRepository.searchAicApi(elasticSearchQuery)) {
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
                    data = networkResponse.data
                )
                Log.i(TAG, "Search Successful:${networkResponse.data.data}")
            }
        }
    }

    // Toggles the view of the Advanced Search
    fun toggleAdvancedSearch() {
        _state.value = (state.value as State.Search).copy(
            showAdvancedSearch = !(state.value as State.Search).showAdvancedSearch
        )
    }

    // Update Advanced Search
    fun updateAdvancedSearchTitle(newTitle: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(title = newTitle)
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

    fun updateAdvancedSearchCategory(newCategory: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(category = newCategory)
            )
        }
        Log.i(TAG, "Advanced search category (department) updated: $newCategory")
    }

    fun updateAdvancedSearchSortBy(newSortBy: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(sortBy = newSortBy)
            )
        }
        Log.i(TAG, "Advanced search sort by updated: $newSortBy")
    }

    fun updateAdvancedSearchSortOrder(newSortOrder: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            if (newSortOrder.lowercase() == "asc") {
                _state.value = currentState.copy(
                    advancedSearchQuery = currentState.advancedSearchQuery.copy(sortOrder = "asc")
                )
            } else {
                _state.value = currentState.copy(
                    advancedSearchQuery = currentState.advancedSearchQuery.copy(sortOrder = "desc")
                )
            }
        }
        Log.i(TAG, "Advanced search sort order updated: $newSortOrder")
    }

    fun onAdvancedSearchFormClear() {
        _state.value = (state.value as State.Search).copy(
            advancedSearchQuery = AicAdvancedSearchQuery()
        )
    }

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    sealed interface State {
        data class Search(
            val data: PaginatedArtworkResults? = null,
            val basicQuery: BasicQuery? = null,
            val advancedSearchQuery: AicAdvancedSearchQuery = AicAdvancedSearchQuery(),
            val isSearching: Boolean = false,
            val showAdvancedSearch: Boolean = false,
            val showSearchRelevance: Boolean = false,
            val showSortOrder: Boolean = false,
            val showApiSource: Boolean = false,
            val showPageLimit: Boolean = false,
            val isUserSignedIn: Boolean = false,
            val pageSize: Int = 20,
            val currentPage: Int = 1,
            val source: String = "AIC",
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