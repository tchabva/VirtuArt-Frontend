package uk.techreturners.virtuart.ui.screens.exhibitions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
        checkCurrentUer()
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

    private fun checkCurrentUer() {
        _state.value = State.Loading
        val user = authRepository.getSignedInUser()
        if (user == null) {
            _state.value = State.NoUser
        } else {
            getAllExhibitions()
        }
    }

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    sealed interface State {
        data object Loading : State

        data object NoUser : State

        data class Loaded(
            val data: List<Exhibition> = emptyList()
        ) : State

        data class Error(val responseCode: Int?, val errorMessage: String) : State

        data class NetworkError(val errorMessage: String) : State
    }

    sealed interface Event {
        // TODO if required
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}