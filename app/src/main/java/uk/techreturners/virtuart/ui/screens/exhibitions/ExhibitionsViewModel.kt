package uk.techreturners.virtuart.ui.screens.exhibitions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.data.model.CreateExhibitionRequest
import uk.techreturners.virtuart.data.model.Exhibition
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.repository.ExhibitionsRepository
import uk.techreturners.virtuart.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ExhibitionsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val exhibitionsRepository: ExhibitionsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.NoUser)
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    init {
        observeUserState()
    }

    private var toDeleteExhibitionId: String? = null

    private fun observeUserState() {
        viewModelScope.launch {
            authRepository.userState.collect { user ->
                if (user == null) {
                    _state.value = State.NoUser
                } else {
                    getAllExhibitions()
                }
            }
        }
    }

    private fun getAllExhibitions() {
        viewModelScope.launch {
            _state.value = State.Loading

            when (val networkResponse = exhibitionsRepository.getAllUserExhibitions()) {
                is NetworkResponse.Exception -> {
                    _state.value = State.NetworkError(
                        errorMessage = networkResponse.exception.message ?: "Unknown Exception"
                    )
                }

                is NetworkResponse.Failed -> {
                    _state.value = State.Error(
                        responseCode = networkResponse.code,
                        errorMessage = networkResponse.message ?: "Unknown Error"
                    )
                }

                is NetworkResponse.Success -> {
                    _state.value = State.Loaded(data = networkResponse.data)
                }
            }
        }
    }

    fun onSignInButtonClicked() {
        viewModelScope.launch {
            emitEvent(Event.GoToSignInButtonClicked)
        }
        Log.i(TAG, "SignInButton clicked")
    }

    fun onCreateExhibitionButtonClicked() {
        viewModelScope.launch {
            val cState = _state.value as State.Loaded

            // Validation of input
            if (cState.exhibitionTitle.isNullOrBlank()) {
                emitEvent(Event.ExhibitionTitleTextFieldEmpty)
            } else {
                // Closes the dialog on clicking save
                dismissCreateExhibitionDialog()

                var description = cState.exhibitionDescription

                // Will send a null description if data is null or blank
                if (description.isNullOrBlank()) {
                    description = null
                }

                val request = CreateExhibitionRequest(
                    title = cState.exhibitionTitle!!,
                    description = description
                )
                Log.i(TAG, "New Exhibition Request: $request")

                postCreateExhibitionRequest(request)
            }
        }
    }

    private suspend fun postCreateExhibitionRequest(request: CreateExhibitionRequest) {
        when (val networkResponse = exhibitionsRepository.createExhibition(request)) {
            is NetworkResponse.Exception -> {
                emitEvent(
                    Event.AddExhibitionNetworkError(
                        message = networkResponse.exception.message ?: "Unknown Exception Occurred"
                    )
                )
                Log.i(
                    TAG,
                    "New Exhibition Request Network Error: ${networkResponse.exception.message}"
                )
            }

            is NetworkResponse.Failed -> {
                emitEvent(
                    Event.AddExhibitionFailed(
                        responseCode = networkResponse.code,
                        message = networkResponse.message ?: "Unknown Exception Occurred"
                    )
                )
                Log.e(
                    TAG,
                    "New Exhibition Request Failed: code: ${networkResponse.code}\n${networkResponse.message}"
                )
            }

            is NetworkResponse.Success -> {
                getAllExhibitions()
                emitEvent(Event.AddExhibitionSuccessful)
                Log.i(TAG, "Exhibition Posted: ${networkResponse.data}")
            }
        }
    }

    fun deleteExhibition(){
        viewModelScope.launch {
            val exhibitionId = toDeleteExhibitionId
            // Only proceed if exhibitionId is not null or blank
            if (exhibitionId.isNullOrBlank()){
                emitEvent(
                    Event.DeleteExhibitionFailed
                )
                Log.e(TAG, "No Exhibition Id set for deletion")
                dismissDeleteExhibitionDialog()
            }else{
                dismissDeleteExhibitionDialog()

                _state.value = State.Loading

                when(val networkResponse = exhibitionsRepository.deleteExhibition(exhibitionId)) {
                    is NetworkResponse.Exception -> {
                        emitEvent(
                            Event.DeleteExhibitionFailedNetwork
                        )
                        Log.e(
                            TAG,
                            "Failed to Delete Album Network Error: ${networkResponse.exception.message}"
                        )
                    }
                    is NetworkResponse.Failed -> {
                        emitEvent(
                            Event.DeleteExhibitionFailed
                        )
                        Log.e(
                            TAG,
                            "Failed to Delete Album Code: ${networkResponse.code}\n${networkResponse.message}"
                        )
                    }
                    is NetworkResponse.Success -> {
                        emitEvent(
                            Event.DeleteExhibitionSuccessful
                        )
                        Log.e(
                            TAG,
                            "Deleted Album Id: $exhibitionId"
                        )
                    }
                }
                getAllExhibitions()
                toDeleteExhibitionId = null
            }
        }
    }

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    fun showCreateExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showCreateExhibitionDialog = true
        )
    }

    fun dismissCreateExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showCreateExhibitionDialog = false
        )
    }

    fun showDeleteExhibitionDialog(exhibitionId: String) {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteExhibitionDialog = true
        )
        toDeleteExhibitionId = exhibitionId
    }

    fun dismissDeleteExhibitionDialog() {
        _state.value = (state.value as State.Loaded).copy(
            showDeleteExhibitionDialog = false
        )
        toDeleteExhibitionId = null
    }

    sealed interface State {
        data object Loading : State

        data object NoUser : State

        data class Loaded(
            val data: List<Exhibition> = emptyList(),
            val showCreateExhibitionDialog: Boolean = false,
            val showDeleteExhibitionDialog: Boolean = false,
            var exhibitionTitle: String? = null,
            var exhibitionDescription: String? = null
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
        data object GoToSignInButtonClicked : Event
        data object AddExhibitionSuccessful : Event
        data class AddExhibitionNetworkError(val message: String) : Event
        data class AddExhibitionFailed(val responseCode: Int?, val message: String) : Event
        data object DeleteExhibitionSuccessful : Event
        data object DeleteExhibitionFailedNetwork : Event
        data object DeleteExhibitionFailed : Event
        data object ExhibitionTitleTextFieldEmpty : Event
        data class ExhibitionItemClicked(val exhibitionId: String) : Event
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}