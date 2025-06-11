package uk.techreturners.virtuart.ui.screens.artworkdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.Exhibition

// Custom Dialog for adding Artworks to Exhibitions
@Composable
fun AddToExhibitionDialog(
    exhibitions: List<Exhibition>,
    onDismiss: () -> Unit,
    onAddToExhibition: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxHeight(.52f)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                // Dialog Header Text
                Text(
                    text = stringResource(R.string.select_an_exhibition),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Exhibitions
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(exhibitions) { exhibition ->
                        AddToExhibitionItem(
                            exhibition = exhibition,
                            onItemClicked = onAddToExhibition
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddToExhibitionDialogPreview() {
    AddToExhibitionDialog(
        exhibitions = listOf(
            Exhibition(
                id = "c99cb367-5f6a-4cdf-9044-f6f7cb9d0519",
                title = "Test Exhibit",
                itemCount = 4,
                createdAt = "2025-06-08T03:35:20.217867",
                updateAt = "2025-06-08T03:35:20.217885"
            ),
            Exhibition(
                id = "c99cb367-5f6a-4cdf-9044-f6f7cb9d0519",
                title = "Test Exhibit",
                itemCount = 4,
                createdAt = "2025-06-08T03:35:20.217867",
                updateAt = "2025-06-08T03:35:20.217885"
            ),
            Exhibition(
                id = "c99cb367-5f6a-4cdf-9044-f6f7cb9d0519",
                title = "Test Exhibit",
                itemCount = 4,
                createdAt = "2025-06-08T03:35:20.217867",
                updateAt = "2025-06-08T03:35:20.217885"
            )
        ),
        onDismiss = {},
        onAddToExhibition = {},
    )
}