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

    init {
        viewModelScope.launch {
            advancedSearchQuery()
        }
    }

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

    private suspend fun advancedSearchQuery(/*advancedSearchQuery: AicAdvancedSearchQuery*/) {
        _state.value = (state.value as State.Search).copy(isSearching = true)

        val elasticSearchQuery = AicApiElasticSearchQuery(
            query = mapOf(
                "bool" to mapOf(
                    "must" to listOf(
                        mapOf("match" to mapOf("artist_title" to "Monet")),
                        mapOf("term" to mapOf("is_public_domain" to true))
                    )
                )
            ),
            sort= listOf(
                mapOf("title.keyword" to mapOf("order" to "desc"))
            ),
            size = 10,
            page = 1
        )

        Log.i(TAG, "Elastic Search Query:\n$elasticSearchQuery")

        when(val networkResponse = artworksRepository.searchAicApi(elasticSearchQuery)){
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
            }
        }
    }

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    sealed interface State {
        data class Search(
            val data: PaginatedArtworkResults? = null,
            val basicQuery: BasicQuery? = null,
            val advancedSearchQuery: AicAdvancedSearchQuery? = null,
            val isSearching: Boolean = false,
            val showAdvancedSearch: Boolean = false,
            val showSearchRelevance: Boolean = false,
            val showSortOrder: Boolean = false,
            val showApiSource: Boolean = false,
            val showPageLimit: Boolean = false,
            val isUserSignedIn: Boolean = false,
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