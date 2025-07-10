package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.ExhibitionDetail
import uk.techreturners.virtuart.data.model.UpdateExhibitionRequest
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.repository.ExhibitionsRepository
import javax.inject.Inject

@HiltViewModel
class ExhibitionDetailViewModel @Inject constructor(
    private val exhibitionsRepository: ExhibitionsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    private var toDeleteArtworkApiId: String? = null
    private var toDeleteArtworkSource: String? = null

    suspend fun getExhibitionDetail(exhibitionId: String) {
        when (val networkResponse = exhibitionsRepository.getExhibitionDetail(exhibitionId)) {
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

    fun deleteExhibition(exhibitionId: String) {
        viewModelScope.launch {
            dismissDeleteExhibitionDialog() // Dismiss the dialog
            // Hold the current state in memory
            val stateHolder: State.Loaded = (state.value as State.Loaded).copy()
            _state.value = State.Loading //

            when (val networkResponse = exhibitionsRepository.deleteExhibition(exhibitionId)) {
                is NetworkResponse.Exception -> {
                    _state.value = stateHolder.copy() // return previous state
                    emitEvent(
                        Event.DeleteExhibitionFailedNetwork
                    )
                    Log.e(
                        TAG,
                        "Failed to delete exhibition," +
                                " Network Error: ${networkResponse.exception.message}"
                    )
                }

                is NetworkResponse.Failed -> {
                    _state.value = stateHolder.copy() // return previous state
                    emitEvent(
                        Event.DeleteExhibitionFailed
                    )
                    Log.e(
                        TAG,
                        "Failed to to delete exhibition" +
                                " Code: ${networkResponse.code}\n${networkResponse.message}"
                    )
                }

                is NetworkResponse.Success -> {
                    emitEvent(
                        Event.DeleteExhibitionSuccessful
                    )
                    Log.e(
                        TAG,
                        "Deleted Exhibition Id: $exhibitionId"
                    )
                }
            }
        }
    }

    fun onUpdateExhibitionButtonClicked(exhibitionId: String) {
        viewModelScope.launch {
            val cState = state.value as State.Loaded
            var title = cState.exhibitionTitle
            var description = cState.exhibitionDescription

            // Validation
            if (title.isNullOrBlank()) {
                emitEvent(Event.ExhibitionTitleTextFieldEmpty)
            } else if (description == null && title.trim() == cState.data.title) {
                emitEvent(Event.ExhibitionDetailsUnchanged)
            } else if (title.trim() == cState.data.title && description != null &&
                description.trim() == cState.data.description
            ) {
                emitEvent(Event.ExhibitionDetailsUnchanged)
            } else {
                dismissUpdateExhibitionDialog()

                if (description.isNullOrBlank() || description == cState.data.description) {
                    description = null
                }

                if (title == cState.data.title) {
                    title = null
                }

                val request = UpdateExhibitionRequest(
                    title = title,
                    description = description
                )

                Log.i(TAG, "Update Exhibition Request: $request")

                updateExhibitionDetails(
                    exhibitionId = exhibitionId,
                    request = request
                )
            }
        }
    }

    private suspend fun updateExhibitionDetails(
        exhibitionId: String,
        request: UpdateExhibitionRequest
    ) {
        when (val networkResponse = exhibitionsRepository.updateExhibitionDetails(
            exhibitionId = exhibitionId,
            request = request
        )) {
            is NetworkResponse.Exception<*> -> {
                emitEvent(
                    Event.ExhibitionDetailsUpdateFailed
                )
                Log.i(
                    TAG,
                    "Update Exhibition Request Network Error: ${networkResponse.exception.message}"
                )
            }

            is NetworkResponse.Failed<*> -> {
                emitEvent(
                    Event.ExhibitionDetailsUpdateFailed
                )
                Log.e(
                    TAG,
                    "Update Exhibition Request Failed: code: ${networkResponse.code}\n${networkResponse.message}"
                )
            }

            is NetworkResponse.Success<*> -> {
                getExhibitionDetail(exhibitionId)
                emitEvent(Event.ExhibitionDetailsUpdatedSuccessfully)
                Log.i(TAG, "Exhibition Updated: ${networkResponse.data}")
            }
        }
    }

    fun deleteArtworkFromExhibition(exhibitionId: String) {
        viewModelScope.launch {
            val apiId = toDeleteArtworkApiId
            val source = toDeleteArtworkSource

            if (apiId.isNullOrBlank() || source.isNullOrBlank()) {
                emitEvent(
                    Event.DeleteExhibitionArtworkItemFailed
                )
                Log.wtf(TAG, "ApiId $apiId or Source $source variable is null")
                dismissDeleteArtworkItemDialog()
            } else {
                dismissUpdateExhibitionDialog()

                _state.value = State.Loading

                when (val networkResponse =
                    exhibitionsRepository.deleteArtworkFromExhibition(
                        exhibitionId = exhibitionId,
                        apiId = apiId,
                        source = source
                    )) {
                    is NetworkResponse.Exception -> {
                        emitEvent(
                            Event.DeleteExhibitionArtworkItemNetworkError
                        )
                        Log.e(
                            TAG,
                            "Failed to delete artwork from exhibition," +
                                    " Network Error: ${networkResponse.exception.message}"
                        )
                    }

                    is NetworkResponse.Failed -> {
                        emitEvent(
                            Event.DeleteExhibitionArtworkItemFailed
                        )
                        Log.e(
                            TAG,
                            "Failed to to delete artwork from exhibition" +
                                    " Code: ${networkResponse.code}\n${networkResponse.message}"
                        )
                    }

                    is NetworkResponse.Success -> {
                        emitEvent(
                            Event.DeleteExhibitionArtworkItemSuccessful
                        )
                        Log.e(
                            TAG,
                            "Deleted artwork apiId: $apiId, source: $source from exhibition Id: $exhibitionId"
                        )
                    }
                }
            }
            getExhibitionDetail(exhibitionId = exhibitionId) // Reload the updated ExhibitionDetail
        }
    }

    fun onDeleteExhibitionConfirmed() {
        viewModelScope.launch {
            emitEvent(
                Event.DeleteExhibitionConfirmed
            )
        }
        Log.i(TAG, "onDeleteExhibitionConfirmed button clicked")
    }

    fun onDeleteArtworkFromExhibitionConfirmed() {
        viewModelScope.launch {
            emitEvent(
                Event.ExhibitionArtworkItemDeleteConfirmed
            )
        }
        Log.i(TAG, "onDeleteArtworkFromExhibitionConfirmed button clicked")
    }

    fun showDeleteExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteExhibitionDialog = true
        )
        Log.i(TAG, "showDeleteExhibitionDialog button clicked")
    }

    fun dismissDeleteExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteExhibitionDialog = false
        )
        Log.i(TAG, "dismissDeleteExhibitionDialog button clicked")
    }

    fun showUpdateExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showUpdateExhibitionDialog = true
        )
        Log.i(TAG, "updateExhibition button clicked")
    }

    fun dismissUpdateExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showUpdateExhibitionDialog = false
        )
        Log.i(TAG, "updateExhibition Dialog dismissed")
    }

    fun updateExhibitionTitle(newString: String) {
        _state.value = (state.value as State.Loaded).copy(
            exhibitionTitle = newString
        )
        Log.i(
            TAG,
            "Exhibition title updated: ${(state.value as State.Loaded).exhibitionTitle}"
        )
    }

    fun updateExhibitionDescription(newString: String) {
        _state.value = (state.value as State.Loaded).copy(
            exhibitionDescription = newString
        )
        Log.i(
            TAG,
            "Exhibition description updated: ${(state.value as State.Loaded).exhibitionDescription}"
        )
    }

    fun onShowDeleteArtworkDialog(apiId: String, source: String) {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteArtworkDialog = true
        )
        toDeleteArtworkApiId = apiId
        toDeleteArtworkSource = source
    }

    fun dismissDeleteArtworkItemDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteArtworkDialog = false
        )
        toDeleteArtworkApiId = null
        toDeleteArtworkSource = null
    }

    fun onExhibitionArtworkItemClicked(apiId: String, source: String) {
        viewModelScope.launch {
            emitEvent(Event.ExhibitionArtworkItemClicked(apiId, source))
        }
    }

    fun onTryAgainButtonCLicked() {
         viewModelScope.launch {
             _state.value = State.Loading
             emitEvent(
                 Event.OnTryAgainButtonClicked
             )
         }
        Log.i(TAG, "Try Again Button clicked")
    }

    sealed interface State {
        data object Loading : State

        data class Loaded(
            val data: ExhibitionDetail,
            val showUpdateExhibitionDialog: Boolean = false,
            val showDeleteExhibitionDialog: Boolean = false,
            val showDeleteArtworkDialog: Boolean = false,
            var exhibitionTitle: String? = data.title,
            var exhibitionDescription: String? = data.description
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
        data object DeleteExhibitionConfirmed : Event
        data object DeleteExhibitionSuccessful : Event
        data object DeleteExhibitionFailedNetwork : Event
        data object DeleteExhibitionFailed : Event
        data object ExhibitionTitleTextFieldEmpty : Event
        data object ExhibitionDetailsUnchanged : Event
        data object ExhibitionDetailsUpdatedSuccessfully : Event
        data object ExhibitionDetailsUpdateFailed : Event
        data class ExhibitionArtworkItemClicked(val apiId: String, val source: String) : Event
        data object ExhibitionArtworkItemDeleteConfirmed : Event
        data object DeleteExhibitionArtworkItemSuccessful : Event
        data object DeleteExhibitionArtworkItemNetworkError : Event
        data object DeleteExhibitionArtworkItemFailed : Event
        data object OnTryAgainButtonClicked : Event
    }

    companion object {
        private const val TAG = "ExhibitionDetailViewModel"
    }
}