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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.repository.ArtworksRepository
import uk.techreturners.virtuart.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ArtworksViewModel @Inject constructor(
    private val repository: ArtworksRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State(
            source = authRepository.source.value
        )
    )
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events
    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    private val _sourceFlow = authRepository.source

    val artworks: Flow<PagingData<ArtworkResult>> =
        _sourceFlow.flatMapLatest { source ->
            // Allows for refresh
            Log.i(TAG, "Current Source API: $source")
            repository.getArtworks(source = source)
        }.cachedIn(viewModelScope)

    fun onArtworkClicked(artworkId: String, source: String) {
        viewModelScope.launch {
            emitEvent(
                event = Event.ClickedOnArtwork(artworkId = artworkId, source = source)
            )
            Log.i(TAG, "Artwork Item clicked\nApiId: $artworkId\nsource: $source")
        }
    }

    // Show ApiSource
    fun toggleShowApiSource() {
        _state.value = state.value.copy(
            showApiSource = !(state.value.showApiSource)
        )
        Log.i(TAG, "Toggle the showApiSource: ${state.value.showApiSource}")
    }

    fun updateApiSource(newSource: String) {
        if (state.value.source != newSource) {
            authRepository.updateSource(newSource)
            _state.value = state.value.copy(
                source = authRepository.source.value
            )
            toggleShowApiSource()
            Log.i(TAG, "Updated the Api Source: ${state.value.source}")
        } else {
            Log.i(TAG, "Api source not updated")
        }
    }

    data class State(
        val showApiSource: Boolean = false,
        val source: String,
        val isRefreshingToken: Boolean = false
    )

    sealed interface Event {
        data class ClickedOnArtwork(val artworkId: String, val source: String) : Event
        data object TokenRefreshFailed : Event
    }

    companion object {
        private const val TAG = "ArtworksViewModel"
    }
}