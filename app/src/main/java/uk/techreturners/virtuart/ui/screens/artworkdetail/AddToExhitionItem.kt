package uk.techreturners.virtuart.ui.screens.artworkdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.data.model.Exhibition

@Composable
internal fun AddToExhibitionItem(
    exhibition: Exhibition,
    onItemClicked: (String) -> Unit
) {
    Card(
        onClick = { onItemClicked(exhibition.id) },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = exhibition.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${exhibition.itemCount} artworks",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddToExhibitionItemPreview() {
    AddToExhibitionItem(
        exhibition = Exhibition(
            id = "c99cb367-5f6a-4cdf-9044-f6f7cb9d0519",
            title = "Test Exhibit",
            itemCount = 4,
            createdAt = "2025-06-08T03:35:20.217867",
            updateAt = "2025-06-08T03:35:20.217885"
        ),
        onItemClicked = {}
    )
}