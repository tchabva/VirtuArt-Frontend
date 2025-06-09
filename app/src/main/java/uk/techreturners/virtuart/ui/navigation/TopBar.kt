package uk.techreturners.virtuart.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TopBar(
    navController: NavController,
    actions: @Composable RowScope.() -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determines whether the topAppBar is shown based on the Destination
    val showTopAppBar = Screens.screensWithTopAppBar.any {
        currentDestination?.hasRoute(it) == true
    }

    // Determines what the title of the TopAppBar will be
    val title = when {
        currentDestination?.hasRoute(Screens.ViewArtwork::class) == true -> {
            "Artwork Details"
        }

        currentDestination?.hasRoute(Screens.ExhibitionDetail::class) == true -> {
            "Exhibition Details"
        }

        else -> ""
    }

    // Animation for the Top Bar
    AnimatedVisibility(
        visible = showTopAppBar,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {

        TopAppBar(
            modifier = Modifier.padding(vertical = 8.dp),
            title = {
                Text(
                    text = title,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = actions
        )
    }
}