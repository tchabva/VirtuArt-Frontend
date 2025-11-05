package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ExhibitionItem

@Composable
fun ExhibitionArtworkItem(
    artwork: ExhibitionItem,
    onClick: (String, String) -> Unit,
    onShowDeleteDialog: (String, String) -> Unit,
) {
    Card(
        onClick = { onClick(artwork.apiId, artwork.source) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Artwork Image
            SubcomposeAsyncImage(
                model = artwork.imageUrl,
                contentDescription = stringResource(
                    R.string.artwork_image_description,
                    artwork.title ?: "Unknown Artwork"
                ),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(40.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                },
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                error = {
                    Image(
                        painter = painterResource(R.drawable.ic_placeholder_artwork),
                        contentDescription = stringResource(
                            R.string.artwork_image_description_error,
                            artwork.title ?: "Unknown Artwork"
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(150.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Artwork Title
                Text(
                    text = artwork.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                // Artwork Artist
                if (!artwork.artist.isNullOrBlank()) {
                    Text(
                        text = artwork.artist,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Artwork Date
                if (!artwork.date.isNullOrBlank()) {
                    Text(
                        text = artwork.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Artwork Source
                Text(
                    text = stringResource(R.string.source_colon_text, artwork.source),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Shows Delete Artwork Confirmation Dialog
            IconButton(onClick = {
                onShowDeleteDialog(
                    artwork.apiId,
                    artwork.source
                )
            }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(
                        R.string.delete_item_content_txt,
                        artwork.title ?: ""
                    ),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExhibitionArtworkItemPreview() {
    ExhibitionArtworkItem(
        artwork = ExhibitionItem(
            id = "item_001",
            apiId = "api_001",
            title = "Sunset Reflections",
            artist = "A Good Painter",
            date = "2025-03-15",
            imageUrl = "https://example.com/images/artwork1.jpg",
            source = "ModernArtAPI",
        ),
        onClick = { _, _ -> },
        onShowDeleteDialog = { _, _ -> },
    )
}
