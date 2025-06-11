package uk.techreturners.virtuart.ui.screens.artworkdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ArtworkDetailScreen(
    viewModel: ArtworkDetailViewModel,
){
    val context = LocalContext.current

    LaunchedEffect(Unit){
        viewModel.events.collect { event ->
            when (event) {
                else -> { /*TODO*/}
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()


    ArtworkDetailScreenContent(state = state.value)
}