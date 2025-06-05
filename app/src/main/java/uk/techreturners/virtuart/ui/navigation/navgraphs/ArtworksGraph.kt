package uk.techreturners.virtuart.ui.navigation.navgraphs

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.CoroutineScope
import uk.techreturners.virtuart.ui.navigation.Screens
import uk.techreturners.virtuart.ui.navigation.Tabs
import uk.techreturners.virtuart.ui.screens.artworks.ArtworksScreen

fun NavGraphBuilder.artworksGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation< Tabs.Artworks>(startDestination = Screens.Artworks) {
        composable<Screens.Artworks> {
            ArtworksScreen()
        }
    }
}