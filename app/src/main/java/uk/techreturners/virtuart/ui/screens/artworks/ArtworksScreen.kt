package uk.techreturners.virtuart.ui.screens.artworks

import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun ArtworksScreen(
    viewModel: ArtworksViewModel,
    onArtworkItemClicked: (String, String) -> Unit
) {
    ArtworksScreenContent(
        artworks = viewModel.artworks.collectAsLazyPagingItems(),
        onArtworkClick = onArtworkItemClicked
    )
}