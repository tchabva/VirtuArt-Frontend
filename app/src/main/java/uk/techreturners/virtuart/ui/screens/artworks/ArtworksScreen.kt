package uk.techreturners.virtuart.ui.screens.artworks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun ArtworksScreen(
    viewModel: ArtworksViewModel,
    onArtworkItemClicked: (String) -> Unit
){

    val state = viewModel.state.collectAsStateWithLifecycle()

    ArtworksScreenContent(
        artworks = viewModel.artworks.collectAsLazyPagingItems(),
        onArtworkClick = { _,_ -> }
    )
}