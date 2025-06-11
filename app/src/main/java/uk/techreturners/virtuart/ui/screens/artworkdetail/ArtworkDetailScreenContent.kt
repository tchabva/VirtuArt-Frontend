package uk.techreturners.virtuart.ui.screens.artworkdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator

@Composable
fun ArtworkDetailScreenContent(
    state: ArtworkDetailViewModel.State
) {
    when (state) {
        is ArtworkDetailViewModel.State.Error -> {
            DefaultErrorScreen(
                responseCode = state.responseCode,
                errorMessage = state.errorMessage
            )
        }

        is ArtworkDetailViewModel.State.Loaded -> {
            ArtworkDetailScreenLoaded(artwork = state.data)
        }

        ArtworkDetailViewModel.State.Loading -> {
            DefaultProgressIndicator()
        }

        is ArtworkDetailViewModel.State.NetworkError -> {
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = state.errorMessage
            )
        }
    }
}

@Composable
private fun ArtworkDetailScreenLoaded(
    artwork: Artwork
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = artwork.toString())
    }
}