package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.data.model.Exhibition

@Composable
fun ExhibitionItem(
    exhibition: Exhibition,
    onClick: (String) -> Unit,
    onDeleteItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp),
        onClick = { onClick(exhibition.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exhibition.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Artworks: ${exhibition.itemCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Created: ${exhibition.createdAt.substring(0, 10)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { onDeleteItemClick(exhibition.id) }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Exhibition",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExhibitionItemPreview() {
    ExhibitionItem(
        exhibition = Exhibition(
            id = "c99cb367-5f6a-4cdf-9044-f6f7cb9d0519",
            title = "Test Exhibit",
            itemCount = 4,
            createdAt = "2025-06-08T03:35:20.217867",
            updateAt = "2025-06-08T03:35:20.217885"
        ),
        onClick = {},
        onDeleteItemClick = {}
    )
}