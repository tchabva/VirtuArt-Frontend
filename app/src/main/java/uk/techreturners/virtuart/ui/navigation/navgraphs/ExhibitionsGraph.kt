package uk.techreturners.virtuart.ui.navigation.navgraphs

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
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
    coroutineScope: CoroutineScope,
    setTopBarActions: (@Composable RowScope.() -> Unit) -> Unit
) {
    navigation<Tabs.Exhibitions>(startDestination = Screens.Exhibitions) {
        composable<Screens.Exhibitions> { backStackEntry ->
            val viewModel = hiltViewModel<ExhibitionsViewModel>()

            // Checks if we are returning from ExhibitionDetail and a refresh is required
            LaunchedEffect(backStackEntry) {
                val savedStateHandle = backStackEntry.savedStateHandle
                val shouldRefresh = savedStateHandle.get<Boolean>("refresh_exhibitions") ?: false
                if (shouldRefresh) {
                    viewModel.refreshExhibitions()
                    savedStateHandle.remove<Boolean>("refresh_exhibitions")
                }
            }

            LaunchedEffect(viewModel.getRefreshExhibitionValue().value) {
                if (viewModel.getRefreshExhibitionValue().value){
                    viewModel.refreshExhibitions()
                    viewModel.resetRefreshExhibition()
                }
            }

            ExhibitionsScreen(
                viewModel = viewModel,
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

            // Sets the actions for the TopAppBar while this screen is active
            DisposableEffect(viewModel) {
                setTopBarActions {
                    IconButton(
                        onClick = viewModel::showUpdateExhibitionDialog
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_exhibition_details_icon_txt)
                        )
                    }
                    IconButton(
                        onClick = viewModel::showDeleteExhibitionDialog
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_exhibition),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                onDispose {
                    setTopBarActions {}
                }
            }

            // Whenever the exhibitionId variable changes this method will be invoked
            LaunchedEffect(exhibitionDetail.exhibitionId) {
                viewModel.getExhibitionDetail(exhibitionId = exhibitionDetail.exhibitionId)
            }

            ExhibitionDetailScreen(
                viewModel = viewModel,
                onArtworkItemClicked = { artworkId, source ->
                    navController.navigate(
                        Screens.ArtworkDetail(
                            artworkId = artworkId,
                            source = source
                        )
                    )
                },
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
                },
                onDeletedExhibitionConfirmed = {
                    viewModel.deleteExhibition(exhibitionDetail.exhibitionId)
                },
                exhibitionDetailsUpdated = { context ->
                    // Set flag for refreshing the Exhibitions when navigating back
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "refresh_exhibitions",
                        true
                    )

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.exhibition_details_updated_success_txt)
                        )
                    }
                },
                exhibitionUpdateRequest = {
                    viewModel.onUpdateExhibitionButtonClicked(exhibitionId = exhibitionDetail.exhibitionId)
                },
                deleteArtworkFromExhibition = {
                    viewModel.deleteArtworkFromExhibition(exhibitionId = exhibitionDetail.exhibitionId)
                },
                onTryAgainButtonClicked = {
                    coroutineScope.launch {
                        viewModel.getExhibitionDetail(
                            exhibitionId = exhibitionDetail.exhibitionId
                        )
                    }
                },
            )
        }
    }
}