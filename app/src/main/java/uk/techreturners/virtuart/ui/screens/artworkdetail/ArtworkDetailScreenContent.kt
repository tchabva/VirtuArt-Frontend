package uk.techreturners.virtuart.ui.screens.artworkdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator

@Composable
fun ArtworkDetailScreenContent(
    state: ArtworkDetailViewModel.State,
    onShowAddToExhibitionDialog: () -> Unit,
    dismissAddToExhibitionDialog: () -> Unit,
    onAddToExhibition: (String) -> Unit

) {
    when (state) {
        is ArtworkDetailViewModel.State.Error -> {
            DefaultErrorScreen(
                responseCode = state.responseCode,
                errorMessage = state.errorMessage
            )
        }

        is ArtworkDetailViewModel.State.Loaded -> {
            ArtworkDetailScreenLoaded(
                state = state,
                onShowAddToExhibitionDialog = onShowAddToExhibitionDialog,
                dismissAddToExhibitionDialog = dismissAddToExhibitionDialog,
                onAddToExhibition = onAddToExhibition
            )
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
    state: ArtworkDetailViewModel.State.Loaded,
    onShowAddToExhibitionDialog: () -> Unit,
    dismissAddToExhibitionDialog: () -> Unit,
    onAddToExhibition: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Artwork Image
            item {
                ArtworkImageCard(state.data)
            }

            // Artwork Information
            item {
                ArtworkInfoCard(state.data)
            }

            // Artwork Details
            item {
                ArtworkDetailsCard(state.data)
            }

            // Description
            if (!state.data.description.isNullOrBlank()) {
                item {
                    ArtworkDescriptionCard(
                        // Remove all HTML tags
                        description = state.data.description.replace(
                            regex = Regex(pattern = "<[^>]*>"),
                            replacement = ""
                        )
                    )
                }
            }

            // Additional Images
            if (state.data.altImageUrls.isNotEmpty()) {
                item {
                    AdditionalImagesCard(state.data)
                }
            }

            // If user is signed in show Add To Exhibition Button
            if (state.isUserSignedIn) {
                item {
                    OutlinedButton(
                        onClick = onShowAddToExhibitionDialog,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
                    ) {
                        Text(
                            text = "Add To Exhibition",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    if (state.showAddToExhibitionDialog) {
        AddToExhibitionDialog(
            exhibitions = state.exhibitions,
            onDismiss = dismissAddToExhibitionDialog,
            onAddToExhibition = onAddToExhibition
        )
    }
}


@Composable
private fun ArtworkImageCard(artwork: Artwork) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        GlideImage(
            model = artwork.imageUrl,
            contentDescription = artwork.title,
            loading = placeholder(R.drawable.ic_placeholder_artwork),
            failure = placeholder(R.drawable.ic_placeholder_artwork),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ArtworkInfoCard(artwork: Artwork) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = artwork.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (!artwork.artist.isNullOrBlank()) {
                Text(
                    text = artwork.artist,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.source, artwork.sourceMuseum),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ArtworkDetailsCard(artwork: Artwork) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.details),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ArtworkDetailRow(stringResource(R.string.date), artwork.date)
            ArtworkDetailRow(stringResource(R.string.medium), artwork.displayMedium)
            ArtworkDetailRow(stringResource(R.string.origin), artwork.origin)
            ArtworkDetailRow(stringResource(R.string.department), artwork.department)
            ArtworkDetailRow(stringResource(R.string.museum), artwork.sourceMuseum)
        }
    }
}

@Composable
private fun ArtworkDetailRow(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(2f)
                )
            }

            // Don't add divider after last item
            if (label != stringResource(R.string.museum)) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
fun ArtworkDescriptionCard(description: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
fun AdditionalImagesCard(artwork: Artwork) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.additional_images),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(artwork.altImageUrls) { imageUrl ->
                    GlideImage(
                        model = imageUrl,
                        contentDescription = stringResource(
                            R.string.additional_images_content_description,
                            artwork.title
                        ),
                        modifier = Modifier
                            .size(168.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtworkDetailScreenLoadedPreview() {
    ArtworkDetailScreenLoaded(
        onShowAddToExhibitionDialog = {},
        dismissAddToExhibitionDialog = {},
        onAddToExhibition = { _-> },
        state = ArtworkDetailViewModel.State.Loaded(
            data = Artwork(
                id = "art001",
                title = "The Starry Night",
                artist = "Vincent van Gogh",
                date = "1889",
                displayMedium = "Oil on canvas",
                imageUrl = "https://example.com/images/starry-night.jpg",
                altImageUrls = listOf(
                    "https://example.com/images/starry-night-detail1.jpg",
                    "https://example.com/images/starry-night-detail2.jpg"
                ),
                description = "A swirling night sky over a quiet village, painted during van Gogh's stay in a mental asylum.",
                origin = "Saint-Rémy-de-Provence, France",
                department = "Post-Impressionism",
                sourceMuseum = "Museum of Modern Art"
            )
        ),
    )
}