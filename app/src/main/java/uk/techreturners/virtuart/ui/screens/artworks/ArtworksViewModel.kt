package uk.techreturners.virtuart.ui.screens.artworks

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.repository.ArtworksRepository
import javax.inject.Inject

@HiltViewModel
class ArtworksViewModel @Inject constructor(
    private val repository: ArtworksRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    val artworks: Flow<PagingData<ArtworkResult>> = repository.getArtworks().cachedIn(viewModelScope)

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    sealed interface State {
        data object Loading : State

        data object PageLoading : State

        data class Loaded(
            val data: PaginatedArtworkResults,
            val showUpdateExhibitionDialog: Boolean = false,
            val showDeleteExhibitionDialog: Boolean = false,
            val showDeleteArtworkDialog: Boolean = false,
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
        data class ClickedOnArtwork(val source: String, val artworkId: String) : Event
    }

    companion object {
        private const val TAG = "ArtworksViewModel"
    }
}