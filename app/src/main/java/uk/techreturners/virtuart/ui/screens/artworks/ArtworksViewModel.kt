package uk.techreturners.virtuart.ui.screens.artworks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.repository.ArtworksRepository
import javax.inject.Inject

@HiltViewModel
class ArtworksViewModel @Inject constructor(
    repository: ArtworksRepository
) : ViewModel() {

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    val artworks: Flow<PagingData<ArtworkResult>> =
        repository.getArtworks().cachedIn(viewModelScope)

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    fun onArtworkClicked(artworkId: String, source: String) {
        viewModelScope.launch {
            emitEvent(
                event = Event.ClickedOnArtwork(artworkId = artworkId, source = source)
            )
            Log.i(TAG, "Artwork Item clicked\nApiId: $artworkId\nsource: $source")
        }
    }

    sealed interface Event {
        data class ClickedOnArtwork(val artworkId: String, val source: String) : Event
    }

    companion object {
        private const val TAG = "ArtworksViewModel"
    }
}