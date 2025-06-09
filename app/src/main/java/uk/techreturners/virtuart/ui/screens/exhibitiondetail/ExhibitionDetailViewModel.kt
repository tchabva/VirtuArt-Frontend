package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uk.techreturners.virtuart.data.model.ExhibitionDetail
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

    fun showDeleteExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteExhibitionDialog = true
        )
    }

    fun dismissDeleteExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteExhibitionDialog = false
        )
    }

    fun showUpdateExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showUpdateExhibitionDialog = true
        )
    }

    fun dismissUpdateExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showUpdateExhibitionDialog = false
        )
    }

    fun onShowDeleteArtworkDialog(apiId: String, source: String) {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteArtworkDialog = true
        )
        toDeleteArtworkApiId = apiId
        toDeleteArtworkSource = source
    }

    fun dismissDeleteArtworkDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteArtworkDialog = true
        )
        toDeleteArtworkApiId = null
        toDeleteArtworkSource = null
    }

    sealed interface State {
        data object Loading : State

        data class Loaded(
            val data: ExhibitionDetail,
            val showUpdateExhibitionDialog: Boolean = false,
            val showDeleteExhibitionDialog: Boolean = false,
            val showDeleteArtworkDialog: Boolean = false,
            var exhibitionTitle: String? = null,
            var exhibitionDescription: String? = null
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
        data object ArtworkDeletedSuccessfully : Event
        data class ArtworkDeletedNetworkError(val message: String) : Event
        data class ArtworkDeletedFailed(val responseCode: Int?, val message: String) : Event
        data object DeleteExhibitionSuccessful : Event
        data object DeleteExhibitionFailedNetwork : Event
        data object DeleteExhibitionFailed : Event
        data object ExhibitionTitleTextFieldEmpty : Event
        data object ExhibitionDetailsUpdatedSuccessfully : Event
        data object ExhibitionDetailsUpdateFailed : Event
        data class ExhibitionItemClicked(val exhibitionId: String) : Event
    }

    companion object {
        private const val TAG = "ExhibitionDetailViewModel"
    }
}