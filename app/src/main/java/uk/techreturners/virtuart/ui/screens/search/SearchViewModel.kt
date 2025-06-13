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
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val artworksRepository: ArtworksRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Search())
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events


    fun onBasicSearch() {
        viewModelScope.launch {
            val cState = state.value as State.Search
            _state.value = (state.value as State.Search).copy(isSearching = true)



            Log.i(TAG, "Advanced Search Query")
        }
    }

    fun onAdvancedSearchFormSubmit() {
        viewModelScope.launch {
            val cState = state.value as State.Search
            val searchRequest = cState.advancedSearchQuery.copy(source = cState.source)

            Log.i(TAG, searchRequest.toString())
            Log.i(TAG, (state.value as State.Search).toString())


            if (
                !searchRequest.title.isNullOrBlank() || !searchRequest.artist.isNullOrBlank() ||
                !searchRequest.medium.isNullOrBlank() || !searchRequest.department.isNullOrBlank()
            ) {
                _state.value = (state.value as State.Search).copy(showAdvancedSearch = false)
                _state.value = (state.value as State.Search).copy(isSearching = true) // Loading Spinner
                searchQuery(searchRequest)
                Log.i(TAG, "Elastic Search Query:\n$searchRequest")
            } else{
                emitEvent(
                    Event.EmptySearchQuery
                )
            }
        }
    }

    private suspend fun searchQuery(query: AdvancedSearchRequest) {
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
                    data = networkResponse.data
                )
                Log.i(TAG, "Search Successful:${networkResponse.data.data}")
            }
        }
    }

    // Update Basic Search
    fun updateBasicSearch(newQuery: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                basicQuery = currentState.basicQuery.copy(query = newQuery)
            )
        }
        Log.i(TAG, "Basic search query updated: $newQuery")
    }

    fun clearBasicSearch() {
        _state.value = (state.value as State.Search).copy(
            advancedSearchQuery = AdvancedSearchRequest()
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

    fun updateAdvancedSearchDepartment(newDepartment: String) {
        val currentState = _state.value
        if (currentState is State.Search) {
            _state.value = currentState.copy(
                advancedSearchQuery = currentState.advancedSearchQuery.copy(department = newDepartment)
            )
        }
        Log.i(TAG, "Advanced search department updated: $newDepartment")
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
            advancedSearchQuery = AdvancedSearchRequest()
        )
        Log.i(TAG, "Cleared Advanced Search TextFields")
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
            val showPageLimit: Boolean = false,
            val isUserSignedIn: Boolean = false,
            val pageSize: Int = 20,
            val source: String = "AIC",
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