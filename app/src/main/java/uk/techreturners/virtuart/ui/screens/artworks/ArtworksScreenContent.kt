package uk.techreturners.virtuart.ui.screens.artworks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.ui.common.ArtworkItem
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator
import uk.techreturners.virtuart.ui.common.DefaultSourceButton
import uk.techreturners.virtuart.ui.common.DefaultSourceDialog

@Composable
fun ArtworksScreenContent(
    state: ArtworksViewModel.State,
    artworks: LazyPagingItems<ArtworkResult>,
    toggleSourceDialog: () -> Unit,
    onUpdateApiSource: (String) -> Unit,
    onArtworkClick: (String, String) -> Unit,
    onTryAgainClicked: () -> Unit
) {
    when (artworks.loadState.refresh) {
        is LoadState.Error -> {
            val e = artworks.loadState.refresh as LoadState.Error
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = e.error.localizedMessage ?: "An error occurred",
                onClick = onTryAgainClicked
            )
        }

        is LoadState.Loading -> {
            ArtworksScreenPageLoading()
        }

        else -> {
            ArtworksScreenLoaded(
                state = state,
                artworks = artworks,
                toggleSourceDialog = toggleSourceDialog,
                onUpdateApiSource = onUpdateApiSource,
                onArtworkClick = onArtworkClick
            )
        }
    }
}

@Composable
private fun ArtworksScreenLoaded(
    state: ArtworksViewModel.State,
    artworks: LazyPagingItems<ArtworkResult>,
    toggleSourceDialog: () -> Unit,
    onUpdateApiSource: (String) -> Unit,
    onArtworkClick: (String, String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.artworks),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            DefaultSourceButton(
                onClick = toggleSourceDialog,
            )
        }

        Spacer(modifier = Modifier.heightIn(8.dp))

        if (artworks.itemCount == 0) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxHeight(0.50f),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No artworks found.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.weight(1f)
            ) {
                // Artworks
                items(
                    count = artworks.itemCount,
                ) { index ->
                    val artwork = artworks[index]
                    if (artwork != null)
                        ArtworkItem(
                            artwork = artwork,
                            onClick = onArtworkClick,
                        )
                }

                // Handle loading state for appending new items
                if (artworks.loadState.append is LoadState.Loading) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            DefaultProgressIndicator()
                        }
                    }
                }
            }
        }
    }
    if (state.showApiSource){
        DefaultSourceDialog(
            onDismiss = toggleSourceDialog,
            onSourceChanged = onUpdateApiSource,
            source = state.source
        )
    }
}

@Composable
fun ArtworksScreenPageLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.artworks),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        DefaultProgressIndicator()
    }
}


@Preview(showBackground = true)
@Composable
private fun ArtworksScreenPageLoadingPreview() {
    ArtworksScreenPageLoading()
}