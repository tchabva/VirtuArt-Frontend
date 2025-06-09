package uk.techreturners.virtuart.ui.screens.exhibitiondetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator

@Composable
fun ExhibitionDetailScreenContent(
    state: ExhibitionDetailViewModel.State,
){
    when (state) {
        is ExhibitionDetailViewModel.State.Error -> {
            
        }
        is ExhibitionDetailViewModel.State.Loaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "${state.data}")
            }
        }
        ExhibitionDetailViewModel.State.Loading -> {
            DefaultProgressIndicator()
        }
        is ExhibitionDetailViewModel.State.NetworkError -> {
            
        }
    }
}