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
import uk.techreturners.virtuart.data.model.AddArtworkRequest
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.model.Exhibition
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

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
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
                    authRepository.userState.collect { user ->
                        if (user == null) {
                            _state.value = State.Loaded(
                                data = networkResponse.data
                            )
                        } else {
                            val exhibitions = getUserExhibitions()
                            _state.value = State.Loaded(
                                data = networkResponse.data,
                                isUserSignedIn = true,
                                exhibitions = exhibitions
                            )
                        }
                        Log.i(
                            TAG,
                            "ExhibitionDetail retrieved: ${networkResponse.data}" +
                                    "\nisUserSignedIn: ${(state.value as State.Loaded).isUserSignedIn}"
                        )
                    }
                }
            }
        }
    }

    private suspend fun getUserExhibitions(): List<Exhibition> {
        var exhibitions: List<Exhibition> = emptyList()
        when (val networkResponse = exhibitionsRepository.getAllUserExhibitions()) {
            is NetworkResponse.Exception -> {
                emitEvent(
                    Event.FailedToRetrieveUserExhibitions
                )
                Log.e(
                    TAG,
                    "Failed to retrieve user's exhibitions:" + networkResponse.exception.message
                )
            }

            is NetworkResponse.Failed -> {
                emitEvent(
                    Event.FailedToRetrieveUserExhibitions
                )
                Log.e(
                    TAG,
                    "Failed to retrieve user's exhibitions:\n" +
                            "code: ${networkResponse.code}\n" +
                            networkResponse.message
                )
            }

            is NetworkResponse.Success -> {
                Log.i(
                    TAG,
                    "Successfully retrieved user's exhibitions:\n" +
                            networkResponse.data
                )
                exhibitions = networkResponse.data
            }
        }
        return exhibitions
    }

    fun addArtworkToExhibition(exhibitionId: String, artworkId: String, source: String) {
        viewModelScope.launch {
            dismissShowAddToExhibitionDialog()
            val request = AddArtworkRequest(apiId = artworkId, source = source)
            when (val networkResponse = exhibitionsRepository.addArtworkToExhibition(
                exhibitionId = exhibitionId,
                request = request
            )) {
                is NetworkResponse.Exception -> {
                    emitEvent(
                        Event.AddToExhibitionFailed
                    )
                    Log.e(
                        TAG,
                        "Failed to add artwork to user's exhibition:" + networkResponse.exception.message
                    )
                }

                is NetworkResponse.Failed -> {
                    if (networkResponse.code == 409) {
                        emitEvent(
                            Event.ArtworkAlreadyInExhibition
                        )
                        Log.e(
                            TAG,
                            "Artwork already exists in the user's exhibition:\n" +
                                    "code: ${networkResponse.code}" +
                                    "\n${networkResponse.message}"
                        )
                    } else {
                        emitEvent(
                            Event.AddToExhibitionFailed
                        )
                        Log.e(
                            TAG,
                            "Artwork already exists in the user's exhibition:\n" +
                                    "code: ${networkResponse.code}" +
                                    "\n${networkResponse.message}"
                        )
                    }
                }

                is NetworkResponse.Success -> {
                    emitEvent(
                        Event.AddToExhibitionSuccessful
                    )
                    Log.e(
                        TAG,
                        "Artwork added to user's exhibition"
                    )
                }
            }
            getUserExhibitions() // Update the User Exhibitions
        }
    }

    fun onAddArtworkToExhibitionItemClicked(exhibitionId: String) {
        viewModelScope.launch {
            emitEvent(
                Event.OnAddToExhibitionClicked(exhibitionId)
            )
        }
        Log.i(TAG, "Exhibition Item $exhibitionId clicked")
    }

    fun onShowAddToExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showAddToExhibitionDialog = true
        )
    }

    fun dismissShowAddToExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showAddToExhibitionDialog = false
        )
    }

    sealed interface State {
        data object Loading : State

        data class Loaded(
            val data: Artwork,
            val showAddToExhibitionDialog: Boolean = false,
            val isUserSignedIn: Boolean = false,
            val exhibitions: List<Exhibition> = emptyList()
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
        data object FailedToRetrieveUserExhibitions : Event
        data class OnAddToExhibitionClicked(val exhibitionId: String) : Event
        data object AddToExhibitionSuccessful : Event
        data object AddToExhibitionFailed : Event
        data object ArtworkAlreadyInExhibition : Event
    }

    companion object {
        private const val TAG = "ArtworkDetailViewModel"
    }
}