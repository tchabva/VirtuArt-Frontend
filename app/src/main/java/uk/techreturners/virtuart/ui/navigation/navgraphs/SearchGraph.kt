package uk.techreturners.virtuart.ui.navigation.navgraphs

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.ui.navigation.Screens
import uk.techreturners.virtuart.ui.navigation.Tabs
import uk.techreturners.virtuart.ui.screens.artworkdetail.ArtworkDetailScreen
import uk.techreturners.virtuart.ui.screens.artworkdetail.ArtworkDetailViewModel
import uk.techreturners.virtuart.ui.screens.search.SearchScreen
import uk.techreturners.virtuart.ui.screens.search.SearchViewModel

fun NavGraphBuilder.searchGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation< Tabs.Search>(startDestination = Screens.Search) {
        composable<Screens.Search> {
            SearchScreen(
                viewModel = hiltViewModel<SearchViewModel>(),
                onArtworkClick = { artworkId, source ->
                    // Navigates to the ArtworkDetail Screen with the artworkId and source
                    navController.navigate(
                        Screens.ArtworkDetail(
                            artworkId = artworkId,
                            source = source
                        )
                    )
                },
            )
        }
    }

    composable<Screens.ArtworkDetail> { backStackEntry ->
        val artworkDetail: Screens.ArtworkDetail = backStackEntry.toRoute()
        val viewModel = hiltViewModel<ArtworkDetailViewModel>()

        LaunchedEffect(artworkDetail.artworkId, artworkDetail.source) {
            viewModel.getArtwork(
                artworkId = artworkDetail.artworkId,
                source = artworkDetail.source
            )
        }

        ArtworkDetailScreen(
            viewModel = viewModel,
            onAddArtworkToExhibition = { exhibitionId ->
                viewModel.addArtworkToExhibition(
                    exhibitionId = exhibitionId,
                    artworkId = artworkDetail.artworkId,
                    source = artworkDetail.source
                )
            },
            addArtworkToExhibitionSuccessful = { context ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.added_artwork_to_exhibition)
                    )
                }
            },
            onTryAgainButtonClicked = {
                viewModel.getArtwork(
                    artworkId = artworkDetail.artworkId,
                    source = artworkDetail.source
                )
            }
        )
    }
}