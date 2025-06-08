package uk.techreturners.virtuart.ui.navigation.navgraphs

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.CoroutineScope
import uk.techreturners.virtuart.ui.navigation.Screens
import uk.techreturners.virtuart.ui.navigation.Tabs
import uk.techreturners.virtuart.ui.screens.exhibitions.ExhibitionsScreen
import uk.techreturners.virtuart.ui.screens.exhibitions.ExhibitionsViewModel

fun NavGraphBuilder.exhibitionsGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation< Tabs.Exhibitions>(startDestination = Screens.Exhibitions) {
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
                }
            )
        }
    }
}