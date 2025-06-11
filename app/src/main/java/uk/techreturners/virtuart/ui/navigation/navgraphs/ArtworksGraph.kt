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
import uk.techreturners.virtuart.ui.navigation.Screens
import uk.techreturners.virtuart.ui.navigation.Tabs
import uk.techreturners.virtuart.ui.screens.artworkdetail.ArtworkDetailScreen
import uk.techreturners.virtuart.ui.screens.artworkdetail.ArtworkDetailViewModel
import uk.techreturners.virtuart.ui.screens.artworks.ArtworksScreen
import uk.techreturners.virtuart.ui.screens.artworks.ArtworksViewModel

fun NavGraphBuilder.artworksGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation<Tabs.Artworks>(startDestination = Screens.Artworks) {
        composable<Screens.Artworks> {
            ArtworksScreen(
                viewModel = hiltViewModel<ArtworksViewModel>(),
                onArtworkItemClicked = { artworkId, source ->
                    // Navigates to the ArtworkDetail Screen with the artworkId and source
                    navController.navigate(
                        Screens.ArtworkDetail(
                            artworkId = artworkId,
                            source = source
                        )
                    )
                }
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
            viewModel = viewModel
        )
    }
}