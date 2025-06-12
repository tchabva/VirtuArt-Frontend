package uk.techreturners.virtuart.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
){
    val state = viewModel.state.collectAsStateWithLifecycle()

    SearchScreenContent(
        state = state.value
    )
}