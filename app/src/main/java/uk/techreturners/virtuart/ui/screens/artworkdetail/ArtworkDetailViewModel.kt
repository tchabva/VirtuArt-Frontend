package uk.techreturners.virtuart.ui.screens.artworkdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.repository.ArtworksRepository
import uk.techreturners.virtuart.data.repository.ExhibitionsRepository
import uk.techreturners.virtuart.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ArtworkDetailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val artworksRepository: ArtworksRepository,
    private val exhibitionsRepository: ExhibitionsRepository
) : ViewModel() {

    init {
        observeUserState()
    }

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    private fun observeUserState() {
        viewModelScope.launch {
            authRepository.userState.collect { user ->
                if (user != null) {
                    if (state.value is State.Loaded) {
                        _state.value = (state.value as State.Loaded).copy(isUserSignedIn = true)
                    }
                    Log.i(TAG, "isUserSigned has been updated: ${_state.value}")
                }
            }
        }
    }

    fun getArtwork(artworkId: String, source: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val networkResponse = artworksRepository.getArtworksById(
                source = source,
                artworkId = artworkId
            )) {
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
                    _state.value = State.Loaded(
                        data = networkResponse.data
                    )
                    Log.i(TAG, "ExhibitionDetail retrieved: ${networkResponse.data}")
                }
            }
        }
    }

    sealed interface State {
        data object Loading : State

        // TODO Exhibition state where you can remove the Artwork from an exhibition as well

        data class Loaded(
            val data: Artwork,
            val showAddToExhibitionDialog: Boolean = false,
            val isUserSignedIn: Boolean = false,
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
       // TODO
    }

    companion object {
        private const val TAG = "ArtworkDetailViewModel"
    }
}