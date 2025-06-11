package uk.techreturners.virtuart.ui.screens.artworks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.ui.common.ArtworkItem
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator
import uk.techreturners.virtuart.ui.common.PaginationControls

@Composable
fun ArtworksScreenContent(
    state: ArtworksViewModel.State,
) {
    when (state) {
        is ArtworksViewModel.State.Error -> {
            DefaultErrorScreen(
                responseCode = state.responseCode,
                errorMessage = state.errorMessage
            )
        }

        is ArtworksViewModel.State.Loaded -> {
            ArtworksScreenLoaded(
                state = state,
                showPageSizeDialog = {},
                onArtworkClick = { _, _ -> },
                onPreviousClick = {},
                onNextClick = {}
            )

        }

        ArtworksViewModel.State.Loading -> {
            DefaultProgressIndicator()
        }

        is ArtworksViewModel.State.NetworkError -> {
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = state.errorMessage
            )
        }

        ArtworksViewModel.State.PageLoading -> {

        }
    }
}

@Composable
private fun ArtworksScreenLoaded(
    state: ArtworksViewModel.State.Loaded,
    showPageSizeDialog: () -> Unit,
    onArtworkClick: (String, String) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO Page Size Button
                OutlinedButton(
                    modifier = Modifier.wrapContentWidth(),
                    onClick = showPageSizeDialog,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.google_logo),
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${state.data.pageSize}/page",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                // TODO source button
            }
        }

        Spacer(modifier = Modifier.heightIn(8.dp))

        if (state.data.data.isEmpty()) {
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
            state.data.let { paginatedArtworkResults ->
                LazyVerticalStaggeredGrid (
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    items(paginatedArtworkResults.data) { artwork ->
                        ArtworkItem(
                            artwork = artwork,
                            onClick = onArtworkClick,
                            source = "aic"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Pagination Controls
                PaginationControls(
                    currentPage = paginatedArtworkResults.currentPage,
                    totalPages = paginatedArtworkResults.totalPages,
                    hasPrevious = paginatedArtworkResults.hasPrevious,
                    hasNext = paginatedArtworkResults.hasNext,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                )
            }
        }
    }
}

@Composable
fun ArtworksScreenPageLoading(){
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO Page Size Button
                OutlinedButton(
                    modifier = Modifier.wrapContentWidth(),
                    onClick = {  },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                    enabled = false
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.google_logo),
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                // TODO source button
            }
        }
        DefaultProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtworksScreenLoadedPreview() {
    ArtworksScreenLoaded(
        state = ArtworksViewModel.State.Loaded(
            data = PaginatedArtworkResults(
                totalItems = 10,
                pageSize = 5,
                totalPages = 2,
                currentPage = 1,
                hasNext = true,
                hasPrevious = false,
                data = listOf(
                    ArtworkResult(
                        id = "1",
                        title = "Starry Night",
                        artistTitle = "Vincent van Gogh",
                        date = "1889",
                        imageURL = "https://example.com/starry-night.jpg"
                    ),
                    ArtworkResult(
                        id = "2",
                        title = "The Persistence of Memory",
                        artistTitle = "Salvador Dalí",
                        date = "1931",
                        imageURL = "https://example.com/persistence-of-memory.jpg"
                    ),
                    ArtworkResult(
                        id = "3",
                        title = "Mona Lisa",
                        artistTitle = "Leonardo da Vinci",
                        date = "1503",
                        imageURL = "https://example.com/mona-lisa.jpg"
                    ),
                    ArtworkResult(
                        id = "4",
                        title = "The Scream",
                        artistTitle = "Edvard Munch",
                        date = "1893",
                        imageURL = "https://example.com/the-scream.jpg"
                    ),
                    ArtworkResult(
                        id = "5",
                        title = "Girl with a Pearl Earring",
                        artistTitle = "Johannes Vermeer",
                        date = "1665",
                        imageURL = "https://example.com/girl-with-pearl-earring.jpg"
                    )
                )
            ),
            showUpdateExhibitionDialog = false,
            showDeleteExhibitionDialog = false,
            showDeleteArtworkDialog = false
        ),
        showPageSizeDialog = { },
        onArtworkClick = { _, _ -> },
        onPreviousClick = {},
        onNextClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ArtworksScreenPageLoadingPreview(){
    ArtworksScreenPageLoading()
}
