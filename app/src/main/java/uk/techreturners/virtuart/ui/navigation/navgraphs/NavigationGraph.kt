package uk.techreturners.virtuart.ui.navigation.navgraphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.CoroutineScope
import uk.techreturners.virtuart.ui.navigation.Tabs

// The Root NavGraph
@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: Any = Tabs.Artworks,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    setTopBarActions: (@Composable RowScope.() -> Unit) -> Unit
) = NavHost(
    modifier = modifier.padding(innerPadding),
    navController = navController,
    startDestination = startDestination

) {
    // Nested Artworks Graph
    artworksGraph(
        navController = navController,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope
    )

    searchGraph(
        navController = navController,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope
    )

    exhibitionsGraph(
        navController = navController,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
        setTopBarActions = setTopBarActions
    )

    profileGraph(
        navController = navController,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope
    )
}