package uk.techreturners.virtuart.ui.screens.exhibitions

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.techreturners.virtuart.R

@Composable
fun ExhibitionsScreen(
    viewModel: ExhibitionsViewModel,
    navigateToProfileGraph: () -> Unit,
    exhibitionCreated: (Context) -> Unit,
    exhibitionDeleted: (Context) -> Unit,
) {
    val context = LocalContext.current

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
                    exhibitionCreated(context)
                }

                ExhibitionsViewModel.Event.DeleteExhibitionFailed -> {
                    Toast.makeText(
                        context,
                        "Failed to delete exhibition",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionsViewModel.Event.DeleteExhibitionSuccessful -> {
                    exhibitionDeleted(context)
                }

                is ExhibitionsViewModel.Event.ExhibitionItemClicked -> {
                    TODO()
                }

                ExhibitionsViewModel.Event.ExhibitionTitleTextFieldEmpty -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.empty_title_warning_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionsViewModel.Event.GoToSignInButtonClicked -> {
                    navigateToProfileGraph()
                }

                ExhibitionsViewModel.Event.DeleteExhibitionFailedNetwork -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.delete_exhibition_network_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ExhibitionsScreenContent(
        state = state.value,
        onSignInClick = viewModel::onSignInButtonClicked,
        onExhibitionClick = { },
        onDeleteExhibitionClick = viewModel::deleteExhibition,
        onShowDeleteExhibitionDialog = viewModel::showDeleteExhibitionDialog,
        onDismissDeleteExhibitionDialog = viewModel::dismissDeleteExhibitionDialog,        
        onCreateNewExhibitionConfirmed = viewModel::onCreateExhibitionButtonClicked,
        onCreateNewExhibitionFabClicked = viewModel::showCreateExhibitionDialog,
        onDismissExhibitionDialog = viewModel::dismissCreateExhibitionDialog
    )
}