package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uk.techreturners.virtuart.data.model.Exhibition
import uk.techreturners.virtuart.data.repository.ExhibitionsRepository
import javax.inject.Inject

@HiltViewModel
class ExhibitionDetailViewModel @Inject constructor(
    private val exhibitionsRepository: ExhibitionsRepository
) : ViewModel(){

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.NoUser)
    val state: StateFlow<State> = _state

    private val _events: MutableSharedFlow<Event> = MutableSharedFlow()
    val events: SharedFlow<Event> = _events

    private suspend fun emitEvent(event: Event) {
        _events.emit(event)
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
        private const val TAG = "ExhibitionDetailViewModel"
    }
}