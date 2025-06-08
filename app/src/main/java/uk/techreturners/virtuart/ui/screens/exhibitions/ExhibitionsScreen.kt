package uk.techreturners.virtuart.ui.screens.exhibitions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ExhibitionsScreen(
    viewModel: ExhibitionsViewModel,
    navigateToProfileGraph: () -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExhibitionsViewModel.Event.AddExhibitionFailed -> {
                    TODO()
                }

                is ExhibitionsViewModel.Event.AddExhibitionNetworkError -> {
                    TODO()
                }

                ExhibitionsViewModel.Event.AddExhibitionSuccessful -> {
                    TODO()
                }

                ExhibitionsViewModel.Event.DeleteExhibitionFailed -> {
                    TODO()
                }

                ExhibitionsViewModel.Event.DeleteExhibitionSuccessful -> {
                    TODO()
                }

                is ExhibitionsViewModel.Event.ExhibitionItemClicked -> {
                    TODO()
                }

                ExhibitionsViewModel.Event.ExhibitionTitleTextFieldEmpty -> {
                    TODO()
                }

                ExhibitionsViewModel.Event.GoToSignInButtonClicked -> {
                    navigateToProfileGraph()
                }
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ExhibitionsScreenContent(
        state = state.value,
        onSignInClick = viewModel::onSignInButtonClicked,
        onExhibitionClick = { },
        onDeleteExhibitionClick = { }
    )
}