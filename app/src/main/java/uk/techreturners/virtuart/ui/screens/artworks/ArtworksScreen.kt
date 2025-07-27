package uk.techreturners.virtuart.ui.screens.artworks

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun ArtworksScreen(
    viewModel: ArtworksViewModel,
    onArtworkItemClicked: (String, String) -> Unit,
    tokenRefreshFailed: (Context) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val artworks = viewModel.artworks.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ArtworksViewModel.Event.ClickedOnArtwork -> {
                    onArtworkItemClicked(event.artworkId, event.source)
                }

                ArtworksViewModel.Event.TokenRefreshFailed -> {
                    tokenRefreshFailed(context)
                }
            }
        }
    }

    ArtworksScreenContent(
        state = state.value,
        artworks = artworks,
        onArtworkClick = viewModel::onArtworkClicked,
        toggleSourceDialog = viewModel::toggleShowApiSource,
        onUpdateApiSource = viewModel::updateApiSource,
        onTryAgainClicked = artworks::retry
    )
}