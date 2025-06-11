package uk.techreturners.virtuart.ui.screens.artworks

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.ui.common.ArtworkItem
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator

@Composable
fun ArtworksScreenContent(
    artworks: LazyPagingItems<ArtworkResult>,
    onArtworkClick: (String, String) -> Unit
) {
    when {
        artworks.loadState.refresh is LoadState.Error -> {
            val e = artworks.loadState.refresh as LoadState.Error
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = e.error.localizedMessage ?: "An error occurred"
            )
        }

        artworks.loadState.refresh is LoadState.Loading -> {
            ArtworksScreenPageLoading()
        }

        else -> {
            ArtworksScreenLoaded(
                artworks = artworks,
                showPageSizeDialog = {},
                onArtworkClick = { _, _ -> }
            )
        }


    }
}

@Composable
private fun ArtworksScreenLoaded(
    artworks: LazyPagingItems<ArtworkResult>,
    showPageSizeDialog: () -> Unit,
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
                            text = "20/page",
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
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.weight(1f)
            ) {
                items(
                    count = artworks.itemCount,
                    key = { index -> 
                        artworks.peek(index)?.id ?: ""
                    }
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
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            DefaultProgressIndicator()
                        }
                    }
                }
            }
        }
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO Page Size Button
                OutlinedButton(
                    modifier = Modifier.wrapContentWidth(),
                    onClick = { },
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

//@Preview(showBackground = true)
//@Composable
//private fun ArtworksScreenLoadedPreview() {
//    ArtworksScreenLoaded(
//        state = ArtworksViewModel.State.Loaded(
//            data = PaginatedArtworkResults(
//                totalItems = 10,
//                pageSize = 5,
//                totalPages = 2,
//                currentPage = 1,
//                hasNext = true,
//                hasPrevious = false,
//                data = listOf(
//                    ArtworkResult(
//                        id = "1",
//                        title = "Starry Night",
//                        artistTitle = "Vincent van Gogh",
//                        date = "1889",
//                        imageURL = "https://example.com/starry-night.jpg",
//                        source = "aic"
//                    ),
//                    ArtworkResult(
//                        id = "2",
//                        title = "The Persistence of Memory",
//                        artistTitle = "Salvador Dalí",
//                        date = "1931",
//                        imageURL = "https://example.com/persistence-of-memory.jpg",
//                        source = "aic"
//                    ),
//                    ArtworkResult(
//                        id = "3",
//                        title = "Mona Lisa",
//                        artistTitle = "Leonardo da Vinci",
//                        date = "1503",
//                        imageURL = "https://example.com/mona-lisa.jpg",
//                        source = "aic"
//                    ),
//                    ArtworkResult(
//                        id = "4",
//                        title = "The Scream",
//                        artistTitle = "Edvard Munch",
//                        date = "1893",
//                        imageURL = "https://example.com/the-scream.jpg",
//                        source = "aic"
//                    ),
//                    ArtworkResult(
//                        id = "5",
//                        title = "Girl with a Pearl Earring",
//                        artistTitle = "Johannes Vermeer",
//                        date = "1665",
//                        imageURL = "https://example.com/girl-with-pearl-earring.jpg",
//                        source = "aic"
//                    )
//                )
//            ),
//            showUpdateExhibitionDialog = false,
//            showDeleteExhibitionDialog = false,
//            showDeleteArtworkDialog = false
//        ),
//        showPageSizeDialog = { },
//        onArtworkClick = { _, _ -> },
//        artworks =,
//    )
//}

@Preview(showBackground = true)
@Composable
private fun ArtworksScreenPageLoadingPreview() {
    ArtworksScreenPageLoading()
}
