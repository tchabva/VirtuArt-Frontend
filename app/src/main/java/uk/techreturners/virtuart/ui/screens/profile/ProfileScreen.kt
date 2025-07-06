package uk.techreturners.virtuart.ui.screens.profile

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onSignInSuccessful: (Context) -> Unit,
    onSignInFailed: (Context) -> Unit,
    onSignOutSuccessful: (Context) -> Unit,
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ProfileViewModel.Event.OnSignInFailed -> {
                    onSignInFailed(context)
                }

                ProfileViewModel.Event.OnSignInSuccessful -> {
                    onSignInSuccessful(context)
                }

                ProfileViewModel.Event.OnSignOutSuccessful -> {
                    onSignOutSuccessful(context)
                }
            }
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle()

    ProfileScreenContent(
        state = state.value,
        onSignIn = viewModel::signIn,
        onSignOut = viewModel::signOut
    )
}