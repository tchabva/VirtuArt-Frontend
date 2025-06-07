package uk.techreturners.virtuart.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    signInButtonClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    ProfileScreenContent(
        state = state.value,
        onSignIn = signInButtonClicked,
        onSignOut = onSignOutClicked
    )
}