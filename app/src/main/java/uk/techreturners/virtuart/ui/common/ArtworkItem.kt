package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
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
            GlideImage(
                model = artwork.imageURL,
                loading = placeholder(R.drawable.ic_placeholder_artwork),
                failure = placeholder(R.drawable.ic_placeholder_artwork),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!artwork.artistTitle.isNullOrBlank()){
                    Text(
                        text = artwork.artistTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!artwork.date.isNullOrBlank()) {
                    Text(
                        text = artwork.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = when(artwork.source) {
                        "aic" -> stringResource(R.string.art_institute_of_chicago)
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
        onClick = {_,_ ->},
    )
}