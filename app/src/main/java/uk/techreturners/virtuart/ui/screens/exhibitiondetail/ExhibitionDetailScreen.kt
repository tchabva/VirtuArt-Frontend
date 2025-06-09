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
                    TODO()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionFailed -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionFailedNetwork -> {
                    TODO()
                }

                ExhibitionDetailViewModel.Event.DeleteExhibitionSuccessful -> {
                    TODO()
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
            }

        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ExhibitionDetailScreenContent(
        state.value,
        onShowDeleteExhibitionDialog = {},
        onDismissDeleteExhibitionDialog = {},
        onDeleteExhibition = {},
        onShowDeleteArtworkDialog = viewModel::onShowDeleteArtworkDialog,
        onDismissDeleteArtworkDialog = {},
        onShowEditExhibitionDialog = {},
        onDismissEditExhibitionDialog = {},
        onUpdateExhibitionClick = {}
    )
}