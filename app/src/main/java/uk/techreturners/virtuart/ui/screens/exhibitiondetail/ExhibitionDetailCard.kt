package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ExhibitionDetail
import uk.techreturners.virtuart.data.model.ExhibitionItem

@Composable
fun ExhibitionDetailCard(
    exhibition: ExhibitionDetail
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = exhibition.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exhibition.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Created Information Colum
                Column {
                    Text(
                        text = stringResource(R.string.created),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = exhibition.createdAt.substring(0, 10),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Last Updated Information Column
                Column {
                    Text(
                        text = stringResource(R.string.last_updated),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = exhibition.updateAt.substring(0, 10),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExhibitionDetailCardPreview() {
    ExhibitionDetailCard(
        exhibition = ExhibitionDetail(
            id = "exhibition_001",
            title = "Modern Art Showcase",
            description = "A curated selection of contemporary artworks from emerging artists.",
            createdAt = "2025-05-01T10:00:00Z",
            updateAt = "2025-06-01T12:30:00Z",
            exhibitionItems = listOf(
                ExhibitionItem(
                    id = "item_001",
                    apiId = "api_001",
                    title = "Sunset Reflections",
                    date = "2025-03-15",
                    imageUrl = "https://example.com/images/artwork1.jpg",
                    source = "ModernArtAPI"
                ),
                ExhibitionItem(
                    id = "item_002",
                    apiId = "api_002",
                    title = "Urban Geometry",
                    date = "2025-04-10",
                    imageUrl = "https://example.com/images/artwork2.jpg",
                    source = "UrbanGallerySource"
                )
            )
        )
    )
}