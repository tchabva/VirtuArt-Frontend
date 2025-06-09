package uk.techreturners.virtuart.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.domain.model.SignInResult
import uk.techreturners.virtuart.domain.model.UserData
import uk.techreturners.virtuart.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.NoUser)
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    init {
        observeUserState()
    }

    fun signIn() {
        viewModelScope.launch {
            _state.value = State.Loading
            when (val signInResult = authRepository.signIn()) {
                is SignInResult.Error -> {
                    _state.value = State.Error(errorMessage = signInResult.exception)
                }

                is SignInResult.Success -> {
                    _state.value = State.SignedIn(signInResult.userData)
                }
                // TODO address the error management more thoroughly
            }
        }
    }

    private fun observeUserState() {
        viewModelScope.launch {
            authRepository.userState.collect { user ->
                if (user == null) _state.value = State.NoUser else
                    _state.value = State.SignedIn(user)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _state.value = State.Loading
            authRepository.signOut()
            _state.value = State.NoUser
        }
    }

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }

    sealed interface State {
        data object Loading : State

        data object NoUser : State

        data class SignedIn(
            val currentUser: UserData? = null,
        ) : State

        data class Error(
            val errorMessage: String
        ) : State
    }

    sealed interface Event {
        // TODO if required
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}