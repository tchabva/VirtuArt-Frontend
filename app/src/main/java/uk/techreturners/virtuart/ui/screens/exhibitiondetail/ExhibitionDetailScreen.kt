package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ExhibitionDetailScreen(
    viewModel: ExhibitionDetailViewModel,
    exhibitionDeleted: (Context) -> Unit,
    artworkDeleted: (Context) -> Unit,
    onDeletedExhibitionConfirmed: () -> Unit

) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExhibitionDetailViewModel.Event.ArtworkDeletedFailed -> {
                    TODO()
                }

                is ExhibitionDetailViewModel.Event.ArtworkDeletedNetworkError -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.ArtworkDeletedSuccessfully -> {

                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionFailed -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionFailedNetwork -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionSuccessful -> {
                    exhibitionDeleted(context)
                }

                ExhibitionDetailViewModel.Event.ExhibitionDetailsUpdateFailed -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.ExhibitionDetailsUpdatedSuccessfully -> {
                    TODO()
                }

                is ExhibitionDetailViewModel.Event.ExhibitionItemClicked -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.ExhibitionTitleTextFieldEmpty -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionConfirmed -> {
                    onDeletedExhibitionConfirmed()
                }
            }

        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ExhibitionDetailScreenContent(
        state.value,
        onDismissDeleteExhibitionDialog = viewModel::dismissDeleteExhibitionDialog,
        onDeleteExhibitionConfirmed = viewModel::onDeleteExhibitionConfirmed,
        onShowDeleteArtworkDialog = viewModel::onShowDeleteArtworkDialog,
        onDismissDeleteArtworkDialog = {},
        onShowEditExhibitionDialog = {},
        onDismissEditExhibitionDialog = {},
        onUpdateExhibitionClick = {}
    )
}