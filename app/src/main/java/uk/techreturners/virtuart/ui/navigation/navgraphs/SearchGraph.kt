package uk.techreturners.virtuart.ui.navigation.navgraphs

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.CoroutineScope
import uk.techreturners.virtuart.ui.navigation.Screens
import uk.techreturners.virtuart.ui.navigation.Tabs
import uk.techreturners.virtuart.ui.screens.search.SearchScreen

fun NavGraphBuilder.searchGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation< Tabs.Search>(startDestination = Screens.Search) {
        composable<Screens.Search> {
            SearchScreen()
        }
    }
}