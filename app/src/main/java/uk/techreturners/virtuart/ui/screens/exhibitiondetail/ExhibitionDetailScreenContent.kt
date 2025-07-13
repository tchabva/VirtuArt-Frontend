package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import uk.techreturners.virtuart.ui.common.DefaultDeleteItemDialog
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultExhibitionEditorDialog
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator

@Composable
fun ExhibitionDetailScreenContent(
    state: ExhibitionDetailViewModel.State,
    onArtworkClick: (String, String) -> Unit,
    onDismissDeleteExhibitionDialog: () -> Unit,
    onDeleteExhibitionConfirmed: () -> Unit,
    onShowDeleteArtworkDialog: (String, String) -> Unit,
    onDismissDeleteArtworkDialog: () -> Unit,
    onDismissEditExhibitionDialog: () -> Unit,
    onUpdateExhibitionClick: () -> Unit,
    updateExhibitionTitle: (String) -> Unit,
    updateExhibitionDescription: (String) -> Unit,
    onDeleteArtworkItemConfirmed: () -> Unit,
    onTryAgainButtonClicked: () -> Unit
) {
    when (state) {
        is ExhibitionDetailViewModel.State.Error, is ExhibitionDetailViewModel.State.NetworkError -> {
            DefaultErrorScreen(
                buttonText = stringResource(R.string.try_again),
                onClick = onTryAgainButtonClicked
            )
        }

        is ExhibitionDetailViewModel.State.Loaded -> {
            ExhibitionDetailScreenLoaded(
                state = state,
                onArtworkClick = onArtworkClick,
                onDeleteExhibitionConfirmed = onDeleteExhibitionConfirmed,
                onDismissDeleteExhibitionDialog = onDismissDeleteExhibitionDialog,
                onShowDeleteArtworkDialog = onShowDeleteArtworkDialog,
                onDismissDeleteArtworkDialog = onDismissDeleteArtworkDialog,
                onDismissEditExhibitionDialog = onDismissEditExhibitionDialog,
                onUpdateExhibitionClick = onUpdateExhibitionClick,
                updateExhibitionTitle = updateExhibitionTitle,
                updateExhibitionDescription = updateExhibitionDescription,
                onDeleteArtworkItemConfirmed = onDeleteArtworkItemConfirmed,
            )
        }

        ExhibitionDetailViewModel.State.Loading -> {
            DefaultProgressIndicator()
        }
    }
}

@Composable
private fun ExhibitionDetailScreenLoaded(
    state: ExhibitionDetailViewModel.State.Loaded,
    onArtworkClick: (String, String) -> Unit,
    onDeleteExhibitionConfirmed: () -> Unit,
    onDismissDeleteExhibitionDialog: () -> Unit,
    onShowDeleteArtworkDialog: (String, String) -> Unit,
    onDismissDeleteArtworkDialog: () -> Unit,
    onDismissEditExhibitionDialog: () -> Unit,
    onUpdateExhibitionClick: () -> Unit,
    updateExhibitionTitle: (String) -> Unit,
    updateExhibitionDescription: (String) -> Unit,
    onDeleteArtworkItemConfirmed: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Exhibition Header Card
        item {
            ExhibitionDetailCard(state.data)
        }

        // Artworks Sections Header
        item {
            Text(
                text = stringResource(R.string.artworks_count_txt, state.data.exhibitionItems.size),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        // Artworks List
        if (state.data.exhibitionItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.no_artworks_in_this_exhibition_yet),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            items(state.data.exhibitionItems) { artwork ->
                ExhibitionArtworkItem(
                    artwork = artwork,
                    onClick = onArtworkClick,
                    onShowDeleteDialog = onShowDeleteArtworkDialog
                )
            }
        }
    }

    if (state.showUpdateExhibitionDialog) {
        DefaultExhibitionEditorDialog(
            exhibitionTitle = state.exhibitionTitle!!,
            exhibitionDescription = state.exhibitionDescription,
            dialogTitle = stringResource(R.string.update_exhibition),
            confirmButtonText = stringResource(R.string.update),
            onDismiss = onDismissEditExhibitionDialog,
            onCreateExhibitionRequestConfirmed = onUpdateExhibitionClick,
            updateTitle = updateExhibitionTitle,
            updateDescription = updateExhibitionDescription
        )
    }

    if (state.showDeleteExhibitionDialog) {
        DefaultDeleteItemDialog(
            title = stringResource(R.string.delete_exhibition),
            alertText = stringResource(R.string.delete_exhibition_alert_dialog_txt),
            onDismiss = onDismissDeleteExhibitionDialog,
            onDeleteItemConfirmed = onDeleteExhibitionConfirmed
        )
    }

    if (state.showDeleteArtworkDialog) {
        DefaultDeleteItemDialog(
            title = "Delete Artwork From Exhibition",
            alertText = "Are you sure that you want to remove this artwork from the exhibition?",
            onDismiss = onDismissDeleteArtworkDialog,
            onDeleteItemConfirmed = onDeleteArtworkItemConfirmed
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExhibitionDetailScreenLoadedPreview() {
    ExhibitionDetailScreenLoaded(
        state = ExhibitionDetailViewModel.State.Loaded(
            data = ExhibitionDetail(
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
                        artist = "A Good Painter",
                        date = "2025-03-15",
                        imageUrl = "https://example.com/images/artwork1.jpg",
                        source = "ModernArtAPI"
                    ),
                    ExhibitionItem(
                        id = "item_002",
                        apiId = "api_002",
                        title = "Urban Geometry",
                        artist = "A Good Photographer",
                        date = "2025-04-10",
                        imageUrl = "https://example.com/images/artwork2.jpg",
                        source = "UrbanGallerySource"
                    )
                )
            )
        ),
        onArtworkClick = { _, _ -> },
        onDeleteExhibitionConfirmed = { },
        onDismissDeleteExhibitionDialog = { },
        onShowDeleteArtworkDialog = { _, _ -> },
        onDismissDeleteArtworkDialog = { },
        onDismissEditExhibitionDialog = { },
        onUpdateExhibitionClick = { },
        updateExhibitionTitle = { },
        updateExhibitionDescription = { },
        onDeleteArtworkItemConfirmed = { },
    )
}