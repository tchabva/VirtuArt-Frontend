package uk.techreturners.virtuart.ui.screens.artworks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun ArtworksScreen(
    viewModel: ArtworksViewModel,
    onArtworkItemClicked: (String, String) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ArtworksViewModel.Event.ClickedOnArtwork -> {
                    onArtworkItemClicked(event.artworkId, event.source)
                }
            }
        }
    }

    ArtworksScreenContent(
        artworks = viewModel.artworks.collectAsLazyPagingItems(),
        onArtworkClick = viewModel::onArtworkClicked
    )
}