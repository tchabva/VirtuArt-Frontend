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
import uk.techreturners.virtuart.ui.screens.exhibitiondetail.ExhibitionDetailScreen
import uk.techreturners.virtuart.ui.screens.exhibitiondetail.ExhibitionDetailViewModel
import uk.techreturners.virtuart.ui.screens.exhibitions.ExhibitionsScreen
import uk.techreturners.virtuart.ui.screens.exhibitions.ExhibitionsViewModel

fun NavGraphBuilder.exhibitionsGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation<Tabs.Exhibitions>(startDestination = Screens.Exhibitions) {
        composable<Screens.Exhibitions> {
            ExhibitionsScreen(
                viewModel = hiltViewModel<ExhibitionsViewModel>(),
                navigateToProfileGraph = {
                    /*
                    Pop everything up to and including the Profile Screen from the backstack and then
                    navigate to then navigate to the Profile Screen
                     */
                    navController.navigate(Screens.Profile) {
                        popUpTo(Screens.Artworks) { inclusive = false }
                    }
                },
                exhibitionCreated = { context ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.new_exhibition_created_snackbar_txt)
                        )
                    }
                },
                exhibitionDeleted = { context ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.deleted_exhibition_successfully)
                        )
                    }
                },
                onExhibitionItemClick = { exhibitionId ->
                    navController.navigate(Screens.ExhibitionDetail(exhibitionId))
                }
            )
        }

        composable<Screens.ExhibitionDetail> { backstackEntry ->
            val exhibitionDetail: Screens.ExhibitionDetail = backstackEntry.toRoute()
            val viewModel = hiltViewModel<ExhibitionDetailViewModel>()

            // Whenever the exhibitionId variable changes this method will be invoked
            LaunchedEffect(exhibitionDetail.exhibitionId) {
                viewModel.getExhibitionDetail(exhibitionId = exhibitionDetail.exhibitionId)
            }

            ExhibitionDetailScreen(
                viewModel = viewModel,
                exhibitionDeleted = { context ->
                    navController.navigate(Screens.Exhibitions) {
                        popUpTo(Screens.Artworks) { inclusive = false }
                    }

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.deleted_exhibition_successfully)
                        )
                    }
                },
                artworkDeleted = { context ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.deleted_artwork_from_exhibition)
                        )
                    }
                }
            )
        }
    }
}