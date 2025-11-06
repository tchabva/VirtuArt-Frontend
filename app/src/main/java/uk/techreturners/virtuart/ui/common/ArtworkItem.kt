package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ArtworkResult

@Composable
fun ArtworkItem(
    artwork: ArtworkResult,
    onClick: (String, String) -> Unit,
) {
    Card(
        onClick = { onClick(artwork.id, artwork.source) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Artwork Image
            SubcomposeAsyncImage(
                model = artwork.imageURL,
                contentDescription = stringResource(
                    R.string.artwork_image_description,
                    artwork.title
                ),
                loading = {
                    DefaultProgressIndicator()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                error = {
                    Image(
                        painter = painterResource(R.drawable.ic_placeholder_artwork),
                        contentDescription = stringResource(
                            R.string.artwork_image_description_error,
                            artwork.title
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(150.dp)
                    )
                }
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Artwork Title
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                // Artwork Artist
                if (!artwork.artistTitle.isNullOrBlank()) {
                    Text(
                        text = artwork.artistTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                // Artwork Date
                if (!artwork.date.isNullOrBlank()) {
                    Text(
                        text = artwork.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Artwork Source
                Text(
                    text = when (artwork.source) {
                        stringResource(R.string.aic) -> stringResource(R.string.aic_full_name)
                        stringResource(R.string.cma) -> stringResource(R.string.cma_full_name)
                        else -> stringResource(R.string.unknown)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtworkItemPreview() {
    ArtworkItem(
        artwork = ArtworkResult(
            id = "5",
            title = "Girl with a Pearl Earring",
            artistTitle = "Johannes Vermeer",
            date = "1665",
            imageURL = "https://example.com/girl-with-pearl-earring.jpg",
            source = "aic"
        ),
        onClick = { _, _ -> },
    )
}