package uk.techreturners.virtuart.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.ui.common.ArtworkItem
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultPageSizeButton
import uk.techreturners.virtuart.ui.common.DefaultSourceButton
import uk.techreturners.virtuart.ui.common.PaginationControls

@Composable
fun SearchScreenContent(
    state: SearchViewModel.State,
    onToggleAdvancedSearch: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onArtistChange: (String) -> Unit = {},
    onMediumChange: (String) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onSortByChange: (String) -> Unit = {},
    onSortOrderChange: (String) -> Unit = {},
    onAdvancedSearch: () -> Unit = {},
    onClearAdvancedSearch: () -> Unit = {}
) {
    when (state) {
        is SearchViewModel.State.Error -> {
            DefaultErrorScreen(
                responseCode = state.responseCode,
                errorMessage = state.errorMessage
            )
        }

        is SearchViewModel.State.NetworkError -> {
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = state.errorMessage
            )
        }

        is SearchViewModel.State.Search -> {
            SearchScreenSearch(
                state = state,
                onToggleAdvancedSearch = onToggleAdvancedSearch,
                onTitleChange = onTitleChange,
                onArtistChange = onArtistChange,
                onMediumChange = onMediumChange,
                onCategoryChange = onCategoryChange,
                onSortByChange = onSortByChange,
                onSortOrderChange = onSortOrderChange,
                onAdvancedSearch = onAdvancedSearch,
                onClearAdvancedSearch = onClearAdvancedSearch
            )
        }
    }
}


@Composable
private fun SearchScreenSearch(
    state: SearchViewModel.State.Search,
    onToggleAdvancedSearch: () -> Unit,
    onTitleChange: (String) -> Unit,
    onArtistChange: (String) -> Unit,
    onMediumChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSortByChange: (String) -> Unit,
    onSortOrderChange: (String) -> Unit,
    onAdvancedSearch: () -> Unit,
    onClearAdvancedSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(.36f),
                text = stringResource(R.string.search_artworks),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Row {
                DefaultPageSizeButton(
                    onClick = {/*TODO*/ },
                    pageSize = state.pageSize
                )

                Spacer(modifier = Modifier.width(8.dp))

                DefaultSourceButton(
                    onClick = { },
                    source = "AIC"
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onToggleAdvancedSearch
                ) {
                    Icon(
                        imageVector = if (state.showAdvancedSearch) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (state.showAdvancedSearch) stringResource(R.string.hide_advanced_search)
                        else stringResource(R.string.show_advanced_search)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (state.showAdvancedSearch) {
            AicAdvancedSearchForm(
                state = state,
                onTitleChange = onTitleChange,
                onArtistChange = onArtistChange,
                onMediumChange = onMediumChange,
                onDepartmentChange = onCategoryChange,
                onSortByChange = onSortByChange,
                onSortOrderChange = onSortOrderChange,
                onSearch = onAdvancedSearch,
                onClear = onClearAdvancedSearch
            )
        }

        if (state.data == null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Enter search terms to find artworks from.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else if (state.data.data.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No artworks found for your search criteria.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            Text(
                text = "Found ${state.data.totalItems} results in from the Art Institute of Chicago",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Results
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.weight(1f)
            ) {
                items(state.data.data) { artwork ->
                    ArtworkItem(
                        artwork = artwork,
                        onClick = { _, _ -> }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            PaginationControls(
                totalPages = state.data.totalPages,
                currentPage = state.data.currentPage,
                hasNext = state.data.hasNext,
                hasPrevious = state.data.hasPrevious,
                onPreviousClick = {/*TODO */ },
                onNextClick = {/*TODO */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenSearchPreview() {
    SearchScreenSearch(
        state = SearchViewModel.State.Search(
            showAdvancedSearch = true
        ),
        onToggleAdvancedSearch = {},
        onTitleChange = {},
        onArtistChange = {},
        onMediumChange = {},
        onCategoryChange = {},
        onSortByChange = {},
        onSortOrderChange = {},
        onAdvancedSearch = {},
        onClearAdvancedSearch = {}
    )
}