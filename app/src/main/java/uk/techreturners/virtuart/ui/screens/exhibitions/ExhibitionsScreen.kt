package uk.techreturners.virtuart.ui.screens.exhibitions

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ExhibitionsScreen(
    viewModel: ExhibitionsViewModel,
    onSignInClicked: () -> Unit
){
    val state = viewModel.state.collectAsStateWithLifecycle()

    ExhibitionsScreenContent(
        state = state.value,
        onClickSign = onSignInClicked
    )
}