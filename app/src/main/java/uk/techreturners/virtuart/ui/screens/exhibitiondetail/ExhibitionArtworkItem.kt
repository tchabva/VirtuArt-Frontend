package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ExhibitionItem

@Composable
fun ExhibitionArtworkItem(
    artwork: ExhibitionItem,
    onClick: (String) -> Unit,
    onShowDeleteDialog: (String, String) -> Unit,
) {
    Card(
        onClick = { onClick(artwork.id) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Artwork
            GlideImage(
                model = artwork.imageUrl,
                contentDescription = artwork.title,
                loading = placeholder(R.drawable.ic_launcher_foreground),
                failure = placeholder(R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                //  TODO possibly add artist variable to ExhibitionItem
                if (artwork.date.isNotBlank()) {
                    Text(
                        text = artwork.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = stringResource(R.string.source_colon_text, artwork.source),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

            }

            // Will show the show the Dialog with to confirm
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
                        artwork.title
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
            date = "2025-03-15",
            imageUrl = "https://example.com/images/artwork1.jpg",
            source = "ModernArtAPI",
        ),
        onClick = { },
        onShowDeleteDialog = { _, _ -> },
    )
}
