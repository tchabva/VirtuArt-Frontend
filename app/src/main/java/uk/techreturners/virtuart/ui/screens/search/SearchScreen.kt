package uk.techreturners.virtuart.ui.screens.search

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onArtworkClick: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchViewModel.Event.ClickedOnArtwork -> {
                    onArtworkClick(event.source, event.artworkId)
                }

                SearchViewModel.Event.EmptySearchQuery -> {
                    Toast.makeText(
                        context,
                        "Please enter search criteria",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    val state = viewModel.state.collectAsStateWithLifecycle()

    SearchScreenContent(
        state = state.value,
        onToggleAdvancedSearch = viewModel::toggleAdvancedSearch,
        onTitleChange = viewModel::updateAdvancedSearchTitle,
        onArtistChange = viewModel::updateAdvancedSearchArtist,
        onMediumChange = viewModel::updateAdvancedSearchMedium,
        onCategoryChange = viewModel::updateAdvancedSearchCategory,
        onSortByChange = viewModel::updateAdvancedSearchSortBy,
        onSortOrderChange = viewModel::updateAdvancedSearchSortOrder,
        onAdvancedSearch = viewModel::onAdvancedSearchFormSubmit,
        onClearAdvancedSearch = viewModel::onAdvancedSearchFormClear
    )
}