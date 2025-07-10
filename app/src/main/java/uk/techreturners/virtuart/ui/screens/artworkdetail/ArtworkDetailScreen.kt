package uk.techreturners.virtuart.ui.screens.artworkdetail

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.techreturners.virtuart.R

@Composable
fun ArtworkDetailScreen(
    viewModel: ArtworkDetailViewModel,
    onAddArtworkToExhibition: (String) -> Unit,
    addArtworkToExhibitionSuccessful: (Context) -> Unit,
    onTryAgainButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ArtworkDetailViewModel.Event.AddToExhibitionFailed -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.failed_to_add_artwork_to_exhibit_toast_txt),
                        Toast.LENGTH_SHORT
                    ).show()
               }
                ArtworkDetailViewModel.Event.AddToExhibitionSuccessful -> {
                    addArtworkToExhibitionSuccessful(context)
                }
                ArtworkDetailViewModel.Event.ArtworkAlreadyInExhibition -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.the_artwork_is_already_in_the_exhibition),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ArtworkDetailViewModel.Event.FailedToRetrieveUserExhibitions -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.failed_to_get_the_users_exhibitions),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ArtworkDetailViewModel.Event.OnAddToExhibitionClicked -> {
                    onAddArtworkToExhibition(event.exhibitionId)
                }

                ArtworkDetailViewModel.Event.OnTryAgainButtonClicked -> {
                    onTryAgainButtonClicked()
                }
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ArtworkDetailScreenContent(
        state = state.value,
        onShowAddToExhibitionDialog = viewModel::onShowAddToExhibitionDialog,
        dismissAddToExhibitionDialog = viewModel::dismissShowAddToExhibitionDialog,
        onAddToExhibition = viewModel::onAddArtworkToExhibitionItemClicked,
        onTryAgainClicked = viewModel::onTryAgainButtonClick
    )
}