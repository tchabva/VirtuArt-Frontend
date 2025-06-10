package uk.techreturners.virtuart.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import uk.techreturners.virtuart.ui.navigation.navgraphs.NavigationGraph

@Composable
fun NavRoot() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var topBarActions: @Composable RowScope.() -> Unit by remember { mutableStateOf({}) }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                actions = topBarActions,
            )
        },
        bottomBar = { BottomNav(navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavigationGraph(
            modifier = Modifier,
            navController = navController,
            innerPadding = innerPadding,
            snackbarHostState = snackbarHostState,
            coroutineScope = scope,
            setTopBarActions = { rowScope ->
                topBarActions = rowScope
            }
        )

    }
}