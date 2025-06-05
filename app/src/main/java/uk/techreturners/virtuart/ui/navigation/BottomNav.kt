package uk.techreturners.virtuart.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@SuppressLint("RestrictedApi")
@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val hideBottomNav = Screens.screensWithoutBottomNav.any {
        currentDestination?.hasRoute(it) == true
    }

    // Animation for the Bottom Navigation Bar
    AnimatedVisibility(
        visible = !hideBottomNav,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        NavigationBar(
            modifier = Modifier.height(80.dp)
        ) {
            topLevelRoute.forEach { topLevelRoute ->
                val isSelected =
                    currentDestination?.hierarchy?.any {
                        it.hasRoute(topLevelRoute.route::class)
                    } == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = topLevelRoute.icon(isSelected),
                            contentDescription = topLevelRoute.name
                        )
                    },
                    label = { Text(topLevelRoute.name) },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(topLevelRoute.route) {
                            Log.i("NavRootNav", topLevelRoute.route.toString())
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

private val topLevelRoute = listOf(
    TopLevelRoute(
        name = "Artworks",
        route = Tabs.Artworks,
        icon = { isSelected ->
            if (isSelected) Icons.Filled.Palette else Icons.Outlined.Palette
        }
    ),
    TopLevelRoute(
        name = "Search",
        route = Tabs.Search,
        icon = { isSelected ->
            if (isSelected) Icons.Filled.Search else Icons.Outlined.Search
        }
    ),
    TopLevelRoute(
        name = "Exhibitions",
        route = Tabs.Exhibitions,
        icon = { isSelected ->
            if (isSelected) Icons.Filled.Bookmarks else Icons.Outlined.Bookmarks
        }
    ),
    TopLevelRoute(
        name = "Profile",
        route = Tabs.Profile,
        icon = { isSelected ->
            if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
        }
    )
)

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    var icon: (Boolean) -> ImageVector
)