package uk.techreturners.virtuart.ui.navigation.navgraphs

import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.ui.navigation.Screens
import uk.techreturners.virtuart.ui.navigation.Tabs
import uk.techreturners.virtuart.ui.screens.profile.ProfileScreen
import uk.techreturners.virtuart.ui.screens.profile.ProfileViewModel

fun NavGraphBuilder.profileGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    navigation< Tabs.Profile>(startDestination = Screens.Profile) {
        composable<Screens.Profile> {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                viewModel = viewModel,
                onSignInSuccessful = { context ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.signed_in_successfully)
                        )
                    }
                },
                onSignOutSuccessful = { context ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.signed_out_successfully)
                        )
                    }
                },
                onSignInFailed = { context ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.sign_in_failed)
                        )
                    }
                }
            )
        }
    }
}