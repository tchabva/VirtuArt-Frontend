package uk.techreturners.virtuart.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.ui.common.ArtworkItem
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultNoArtworksCard
import uk.techreturners.virtuart.ui.common.DefaultPageSizeButton
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator
import uk.techreturners.virtuart.ui.common.DefaultSourceButton
import uk.techreturners.virtuart.ui.common.DefaultSourceDialog

@Composable
fun SearchScreenContent(
    state: SearchViewModel.State.Search,
    searchMetadata: PaginatedArtworkResults?,
    hasActiveSearch: Boolean,
    artworks: LazyPagingItems<ArtworkResult>,
    onToggleAdvancedSearch: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onArtistChange: (String) -> Unit = {},
    onMediumChange: (String) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onSortByChange: (String) -> Unit = {},
    onSortOrderChange: (String) -> Unit = {},
    onAdvancedSearch: () -> Unit = {},
    onClearAdvancedSearch: () -> Unit = {},
    onClearBasicSearch: () -> Unit = {},
    onBasicSearch: () -> Unit = {},
    onBasicQueryChange: (String) -> Unit = {},
    onArtworkItemClick: (String, String) -> Unit = { _, _ -> },
    toggleApiSourceDialog: () -> Unit = {},
    onUpdateApiSource: (String) -> Unit,
    togglePageSizeDialog: () -> Unit,
    onUpdatePageSize: (Int) -> Unit,
    onReturnToSearchClicked: () -> Unit,
) {
    SearchScreenSearch(
        state = state,
        searchMetadata = searchMetadata,
        hasActiveSearch = hasActiveSearch,
        artworks = artworks,
        onToggleAdvancedSearch = onToggleAdvancedSearch,
        onTitleChange = onTitleChange,
        onArtistChange = onArtistChange,
        onMediumChange = onMediumChange,
        onCategoryChange = onCategoryChange,
        onSortByChange = onSortByChange,
        onSortOrderChange = onSortOrderChange,
        onAdvancedSearch = onAdvancedSearch,
        onClearAdvancedSearch = onClearAdvancedSearch,
        onClearBasicSearch = onClearBasicSearch,
        onBasicSearch = onBasicSearch,
        onBasicQueryChange = onBasicQueryChange,
        onArtworkItemClick = onArtworkItemClick,
        toggleApiSourceDialog = toggleApiSourceDialog,
        onUpdateApiSource = onUpdateApiSource,
        togglePageSizeDialog = togglePageSizeDialog,
        onUpdatePageSize = onUpdatePageSize,
        onReturnToSearchClicked = onReturnToSearchClicked,
    )
}

@Composable
private fun SearchScreenSearch(
    state: SearchViewModel.State.Search,
    searchMetadata: PaginatedArtworkResults?,
    hasActiveSearch: Boolean,
    artworks: LazyPagingItems<ArtworkResult>,
    onToggleAdvancedSearch: () -> Unit,
    onTitleChange: (String) -> Unit,
    onArtistChange: (String) -> Unit,
    onMediumChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSortByChange: (String) -> Unit,
    onSortOrderChange: (String) -> Unit,
    onAdvancedSearch: () -> Unit,
    onClearAdvancedSearch: () -> Unit,
    onClearBasicSearch: () -> Unit,
    onBasicSearch: () -> Unit,
    onBasicQueryChange: (String) -> Unit,
    onArtworkItemClick: (String, String) -> Unit = { _, _ -> },
    toggleApiSourceDialog: () -> Unit,
    onUpdateApiSource: (String) -> Unit,
    togglePageSizeDialog: () -> Unit,
    onUpdatePageSize: (Int) -> Unit,
    onReturnToSearchClicked: () -> Unit,
) {
    val isSearchLoading =
        hasActiveSearch && artworks.loadState.refresh is LoadState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(.4f),
                text = stringResource(R.string.search_artworks),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefaultPageSizeButton(
                    onClick = togglePageSizeDialog,
                    pageSize = state.pageSize
                )

                Spacer(modifier = Modifier.width(8.dp))

                DefaultSourceButton(
                    onClick = toggleApiSourceDialog
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onToggleAdvancedSearch,
                    enabled = state.source == stringResource(R.string.aic)
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
                isSearchLoading = isSearchLoading,
                onTitleChange = onTitleChange,
                onArtistChange = onArtistChange,
                onMediumChange = onMediumChange,
                onDepartmentChange = onCategoryChange,
                onSortByChange = onSortByChange,
                onSortOrderChange = onSortOrderChange,
                onSearch = onAdvancedSearch,
                onClear = onClearAdvancedSearch
            )

            Spacer(modifier = Modifier.height(16.dp))
        } else {
            SimpleSearchForm(
                state = state,
                isSearchLoading = isSearchLoading,
                onQueryChange = onBasicQueryChange,
                onSearch = onBasicSearch,
                onClear = onClearBasicSearch
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        when {
            !hasActiveSearch -> {
                SearchIdleHint(state = state)
            }

            artworks.loadState.refresh is LoadState.Error -> {
                DefaultErrorScreen(
                    buttonText = stringResource(R.string.return_to_search),
                    onClick = onReturnToSearchClicked
                )
            }

            artworks.loadState.refresh is LoadState.Loading && artworks.itemCount == 0 -> {
                DefaultProgressIndicator()
            }

            searchMetadata != null && searchMetadata.totalItems == 0 -> {
                SearchResultsHeader(
                    state = state,
                    totalItems = 0,
                )
                Spacer(modifier = Modifier.height(16.dp))
                DefaultNoArtworksCard()
            }

            else -> {
                val totalItems = searchMetadata?.totalItems ?: artworks.itemCount
                SearchResultsHeader(
                    state = state,
                    totalItems = totalItems,
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (artworks.itemCount == 0) {
                    DefaultProgressIndicator()
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(150.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(
                            count = artworks.itemCount,
                        ) { index ->
                            val artwork = artworks[index]
                            if (artwork != null) {
                                ArtworkItem(
                                    artwork = artwork,
                                    onClick = onArtworkItemClick,
                                )
                            }
                        }

                        if (artworks.loadState.append is LoadState.Loading) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    DefaultProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.showApiSource) {
        DefaultSourceDialog(
            onDismiss = toggleApiSourceDialog,
            onSourceChanged = onUpdateApiSource,
            source = state.source
        )
    }

    if (state.showPageSize) {
        PageSizeDialog(
            onDismiss = togglePageSizeDialog,
            onPageSizeChanged = onUpdatePageSize,
            state = state
        )
    }
}

@Composable
private fun SearchIdleHint(state: SearchViewModel.State.Search) {
    val source = when (state.source) {
        stringResource(R.string.aic) -> stringResource(R.string.aic_full_name)
        stringResource(R.string.cma) -> stringResource(R.string.cma_full_name)
        else -> stringResource(R.string.unknown)
    }
    Text(
        text = stringResource(R.string.source_museum_text, source),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.enter_search_terms_txt),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SearchResultsHeader(
    state: SearchViewModel.State.Search,
    totalItems: Int,
) {
    val source = when (state.source) {
        stringResource(R.string.aic) -> stringResource(R.string.aic_full_name)
        stringResource(R.string.cma) -> stringResource(R.string.cma_full_name)
        else -> stringResource(R.string.unknown)
    }
    val text = if (totalItems == 0) {
        stringResource(R.string.found_no_results_src_txt, source)
    } else {
        stringResource(
            R.string.found_results_from_the_src_txt,
            totalItems,
            source
        )
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
