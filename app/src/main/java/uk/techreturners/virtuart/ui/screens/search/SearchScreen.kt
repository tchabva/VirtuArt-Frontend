package uk.techreturners.virtuart.ui.screens.search

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import uk.techreturners.virtuart.R

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onArtworkClick: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val currentContext by rememberUpdatedState(context)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchViewModel.Event.ClickedOnArtwork -> {
                    onArtworkClick(event.source, event.artworkId)
                }

                SearchViewModel.Event.EmptySearchQuery -> {
                    Toast.makeText(
                        currentContext,
                        currentContext.getString(R.string.please_enter_search_criteria_txt),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchMetadata by viewModel.searchMetadata.collectAsStateWithLifecycle()
    val hasActiveSearch by viewModel.hasActiveSearch.collectAsStateWithLifecycle()
    val artworks = viewModel.searchResults.collectAsLazyPagingItems()

    val searchState = when (val s = state) {
        is SearchViewModel.State.Search -> s
    }

    SearchScreenContent(
        state = searchState,
        searchMetadata = searchMetadata,
        hasActiveSearch = hasActiveSearch,
        artworks = artworks,
        onToggleAdvancedSearch = viewModel::toggleAdvancedSearch,
        onTitleChange = viewModel::updateAdvancedSearchTitle,
        onArtistChange = viewModel::updateAdvancedSearchArtist,
        onMediumChange = viewModel::updateAdvancedSearchMedium,
        onCategoryChange = viewModel::updateAdvancedSearchDepartment,
        onSortByChange = viewModel::updateAdvancedSearchSortBy,
        onSortOrderChange = viewModel::updateAdvancedSearchSortOrder,
        onAdvancedSearch = viewModel::onAdvancedSearchFormSubmit,
        onClearAdvancedSearch = viewModel::onAdvancedSearchFormClear,
        onClearBasicSearch = viewModel::clearBasicSearch,
        onBasicSearch = viewModel::onBasicSearch,
        onBasicQueryChange = viewModel::updateBasicSearch,
        onArtworkItemClick = onArtworkClick,
        toggleApiSourceDialog = viewModel::toggleShowApiSourceDialog,
        onUpdateApiSource = viewModel::updateApiSource,
        togglePageSizeDialog = viewModel::toggleShowPageSizeDialog,
        onUpdatePageSize = viewModel::updatePageSize,
        onReturnToSearchClicked = viewModel::onReturnToSearchButtonClicked
    )
}
