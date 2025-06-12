package uk.techreturners.virtuart.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreenContent(
    state: SearchViewModel.State
){

    when(state) {
        is SearchViewModel.State.Error -> {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Search Screen"
            )
        }
        is SearchViewModel.State.NetworkError -> {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Search Screen"
            )
        }
        is SearchViewModel.State.Search -> {
            Column (Modifier.padding(16.dp)){
                state.data?.data?.forEach{ artworkResult ->
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "$artworkResult"
                    )
                }
            }
        }
    }
}