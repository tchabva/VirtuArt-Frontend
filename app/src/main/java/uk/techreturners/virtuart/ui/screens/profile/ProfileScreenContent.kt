package uk.techreturners.virtuart.ui.screens.profile

import androidx.compose.runtime.Composable

@Composable
fun ProfileScreenContent(
    state: ProfileViewModel.State,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {

    when (state) {
        is ProfileViewModel.State.Error -> TODO()
        ProfileViewModel.State.Loading -> TODO()
        ProfileViewModel.State.NoUser -> TODO()
        is ProfileViewModel.State.SignedIn -> TODO()
    }
}