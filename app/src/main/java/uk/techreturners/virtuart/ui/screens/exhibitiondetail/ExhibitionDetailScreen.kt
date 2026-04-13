package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.techreturners.virtuart.R

@Composable
fun ExhibitionDetailScreen(
    viewModel: ExhibitionDetailViewModel,
    onArtworkItemClicked: (String, String) -> Unit,
    exhibitionDeleted: (Context) -> Unit,
    artworkDeleted: (Context) -> Unit,
    onDeletedExhibitionConfirmed: () -> Unit,
    exhibitionDetailsUpdated: (Context) -> Unit,
    exhibitionUpdateRequest: () -> Unit,
    deleteArtworkFromExhibition: () -> Unit,
    onTryAgainButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val currentContext by rememberUpdatedState(context)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExhibitionDetailViewModel.Event.DeleteExhibitionArtworkItemFailed -> {
                    Toast.makeText(
                        currentContext,
                        currentContext.getString(R.string.failed_to_delete_artwork_from_the_exhibition),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ExhibitionDetailViewModel.Event.DeleteExhibitionArtworkItemNetworkError -> {
                    Toast.makeText(
                        currentContext,
                        currentContext.getString(R.string.exhibition_update_failed_due_to_network_error_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionArtworkItemSuccessful -> {
                    artworkDeleted(context)
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionFailed -> {
                    Toast.makeText(
                        currentContext,
                        currentContext.getString(R.string.failed_to_delete_exhibition_toast_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionFailedNetwork -> {
                    Toast.makeText(
                        currentContext,
                        currentContext.getString(R.string.delete_exhibition_network_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionSuccessful -> {
                    exhibitionDeleted(context)
                }

                ExhibitionDetailViewModel.Event.ExhibitionDetailsUpdateFailed -> {
                    Toast.makeText(
                        currentContext,
                        currentContext.getString(R.string.failed_to_update_exhibition_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionDetailViewModel.Event.ExhibitionDetailsUpdatedSuccessfully -> {
                    exhibitionDetailsUpdated(context)
                }

                is ExhibitionDetailViewModel.Event.ExhibitionArtworkItemClicked -> {
                    onArtworkItemClicked(event.apiId, event.source)
                }

                ExhibitionDetailViewModel.Event.ExhibitionTitleTextFieldEmpty -> {
                    Toast.makeText(
                        context,
                        currentContext.getString(R.string.empty_title_warning_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionConfirmed -> {
                    onDeletedExhibitionConfirmed()
                }

                ExhibitionDetailViewModel.Event.ExhibitionDetailsUnchanged -> {
                    Toast.makeText(
                        currentContext,
                        currentContext.getString(R.string.there_are_no_changes_to_update),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ExhibitionDetailViewModel.Event.ExhibitionArtworkItemDeleteConfirmed -> {
                    deleteArtworkFromExhibition()
                }

                ExhibitionDetailViewModel.Event.OnTryAgainButtonClicked -> {
                    onTryAgainButtonClicked()
                }
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ExhibitionDetailScreenContent(
        state.value,
        onArtworkClick = viewModel::onExhibitionArtworkItemClicked,
        onDismissDeleteExhibitionDialog = viewModel::dismissDeleteExhibitionDialog,
        onDeleteExhibitionConfirmed = viewModel::onDeleteExhibitionConfirmed,
        onShowDeleteArtworkDialog = viewModel::onShowDeleteArtworkDialog,
        onDismissDeleteArtworkDialog = viewModel::dismissDeleteArtworkItemDialog,
        onDismissEditExhibitionDialog = viewModel::dismissUpdateExhibitionDialog,
        onUpdateExhibitionClick = exhibitionUpdateRequest,
        updateExhibitionTitle = viewModel::updateExhibitionTitle,
        updateExhibitionDescription = viewModel::updateExhibitionDescription,
        onDeleteArtworkItemConfirmed = viewModel::onDeleteArtworkFromExhibitionConfirmed,
        onTryAgainButtonClicked = viewModel::onTryAgainButtonCLicked
    )
}