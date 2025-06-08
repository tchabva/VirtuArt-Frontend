package uk.techreturners.virtuart.ui.screens.exhibitions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator

@Composable
fun ExhibitionsScreenContent(
    state: ExhibitionsViewModel.State,
    onClickSign: () -> Unit
){

    when (state){
        is ExhibitionsViewModel.State.Error -> {
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = state.errorMessage
            )
        }
        is ExhibitionsViewModel.State.Loaded -> {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(state.data.toString())
            }
        }
        ExhibitionsViewModel.State.Loading -> {
            DefaultProgressIndicator()
        }
        is ExhibitionsViewModel.State.NetworkError -> {
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = state.errorMessage
            )
        }
        ExhibitionsViewModel.State.NoUser -> {
            Column {
                Text("NO USER")
                Text(stringResource(R.string.dev_server_ip))
                Text(stringResource(R.string.local_server_ip))
            }

        }
    }
}