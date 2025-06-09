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
    onExhibitionItemClick: (String) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExhibitionsViewModel.Event.AddExhibitionFailed -> {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.failed_to_add_exhibition_http_code_toast_txt,
                            event.responseCode.toString(),
                            event.message
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ExhibitionsViewModel.Event.AddExhibitionNetworkError -> {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.failed_to_add_exhibition_due_to_network_error,
                            event.message
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionsViewModel.Event.AddExhibitionSuccessful -> {
                    exhibitionCreated(context)
                }

                ExhibitionsViewModel.Event.DeleteExhibitionFailed -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.failed_to_delete_exhibition_toast_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionsViewModel.Event.DeleteExhibitionSuccessful -> {
                    exhibitionDeleted(context)
                }

                is ExhibitionsViewModel.Event.ExhibitionItemClicked -> {
                    onExhibitionItemClick(event.exhibitionId)
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
        onExhibitionClick = viewModel::onExhibitionItemClicked,
        onDeleteExhibitionClick = viewModel::deleteExhibition,
        onShowDeleteExhibitionDialog = viewModel::showDeleteExhibitionDialog,
        onDismissDeleteExhibitionDialog = viewModel::dismissDeleteExhibitionDialog,
        onCreateNewExhibitionConfirmed = viewModel::onCreateExhibitionButtonClicked,
        onCreateNewExhibitionFabClicked = viewModel::showCreateExhibitionDialog,
        onDismissExhibitionDialog = viewModel::dismissCreateExhibitionDialog
    )
}