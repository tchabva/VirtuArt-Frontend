package uk.techreturners.virtuart.ui.screens.artworks

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import retrofit2.HttpException

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

    val loadState = artworks.loadState.refresh
    if (loadState is LoadState.Error) {
        val error = loadState.error
        if (error is HttpException && error.code() == 401) {
            LaunchedEffect(loadState) {
                viewModel.onTokenExpired()
            }
        }
    }

    if (state.value.isRefreshingToken) {
        ArtworksScreenPageLoading()
    } else {
        ArtworksScreenContent(
            state = state.value,
            artworks = artworks,
            onArtworkClick = viewModel::onArtworkClicked,
            toggleSourceDialog = viewModel::toggleShowApiSource,
            onUpdateApiSource = viewModel::updateApiSource,
            onTryAgainClicked = viewModel::onTryAgainButtonClick
        )
    }
}