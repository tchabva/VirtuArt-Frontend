package uk.techreturners.virtuart.ui.screens.profile

import android.util.Log
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
            Log.i(TAG, "Sign In Button clicked")
            when (val signInResult = authRepository.signIn()) {
                is SignInResult.Error -> {
                    _state.value = State.NoUser
                    emitEvent(
                        Event.OnSignInFailed
                    )
                }

                is SignInResult.Success -> {
                    _state.value = State.SignedIn(signInResult.userData)
                    emitEvent(
                        Event.OnSignInSuccessful
                    )
                }
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
            emitEvent(
                Event.OnSignOutSuccessful
            )
        }
        Log.i(TAG, "Sign Out Button clicked")
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
    }

    sealed interface Event {
        data object OnSignInSuccessful : Event
        data object OnSignInFailed : Event
        data object OnSignOutSuccessful : Event
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}