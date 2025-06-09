package uk.techreturners.virtuart.ui.screens.exhibitions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.Exhibition
import uk.techreturners.virtuart.ui.common.DefaultDeleteItemDialog
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator
import uk.techreturners.virtuart.ui.common.ExhibitionItem

@Composable
fun ExhibitionsScreenContent(
    state: ExhibitionsViewModel.State,
    onSignInClick: () -> Unit,
    onExhibitionClick: (String) -> Unit,
    onDeleteExhibitionClick: () -> Unit,
    onDismissExhibitionDialog: () -> Unit,
    onCreateNewExhibitionFabClicked: () -> Unit,
    onCreateNewExhibitionConfirmed: () -> Unit,
    onShowDeleteExhibitionDialog: (String) -> Unit,
    onDismissDeleteExhibitionDialog: () -> Unit,
) {

    when (state) {
        is ExhibitionsViewModel.State.Error -> {
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = state.errorMessage
            )
        }

        is ExhibitionsViewModel.State.Loaded -> {
            ExhibitionsScreenLoaded(
                state = state,
                onExhibitionClick = onExhibitionClick,
                onDeleteExhibitionClick = onDeleteExhibitionClick,
                onDismissCreateExhibitionDialog = onDismissExhibitionDialog,
                onCreateNewExhibitionFabClicked = onCreateNewExhibitionFabClicked,
                onCreateNewExhibitionConfirmed = onCreateNewExhibitionConfirmed,
                onDismissDeleteExhibitionDialog = onDismissDeleteExhibitionDialog,
                onShowDeleteExhibitionDialog = onShowDeleteExhibitionDialog
            )
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
            ExhibitionScreenNoUser(onSignInClick = onSignInClick)
        }
    }
}

@Composable
private fun ExhibitionsScreenLoaded(
    state: ExhibitionsViewModel.State.Loaded,
    onExhibitionClick: (String) -> Unit,
    onDeleteExhibitionClick: () -> Unit,
    onDismissCreateExhibitionDialog: () -> Unit,
    onDismissDeleteExhibitionDialog: () -> Unit,
    onCreateNewExhibitionFabClicked: () -> Unit,
    onCreateNewExhibitionConfirmed: () -> Unit,
    onShowDeleteExhibitionDialog: (String) -> Unit,

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.my_exhibitions_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            FloatingActionButton(
                onClick = onCreateNewExhibitionFabClicked,
                modifier = Modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = stringResource(R.string.create_exhibition_button_txt)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // When there are no Exhibitions displays a message to the user or shows the Exhibitions
        if (state.data.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = stringResource(R.string.no_exhibitions_text),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 24.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.data) { exhibition ->
                    ExhibitionItem(
                        exhibition = exhibition,
                        onClick = onExhibitionClick,
                        onDeleteItemClick = onShowDeleteExhibitionDialog
                    )
                }
            }
        }
    }
    if (state.showCreateExhibitionDialog){
        CreateExhibitionDialog(
            state = state,
            onDismiss = onDismissCreateExhibitionDialog,
            onCreateExhibitionRequestConfirmed = onCreateNewExhibitionConfirmed
        )
    }

    if (state.showDeleteExhibitionDialog){
        DefaultDeleteItemDialog(
            title = stringResource(R.string.delete_exhibition),
            alertText = stringResource(R.string.delete_exhibition_alert_dialog_txt),
            onDismiss = onDismissDeleteExhibitionDialog,
            onDeleteItemConfirmed = onDeleteExhibitionClick
        ) 
    }
}

@Composable
private fun ExhibitionScreenNoUser(
    onSignInClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.my_exhibitions_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = stringResource(R.string.no_user_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text(
                    text = "Go To Sign In",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExhibitionsScreenLoadedPreview() {
    ExhibitionsScreenLoaded(
        state = ExhibitionsViewModel.State.Loaded(
            data = listOf(
                Exhibition(
                    id = "c99cb367-5f6a-4cdf-9044-f6f7cb9d0519",
                    title = "Test Exhibit",
                    itemCount = 4,
                    createdAt = "2025-06-08T03:35:20.217867",
                    updateAt = "2025-06-08T03:35:20.217885"
                )
            )
        ),
        onExhibitionClick = {},
        onDeleteExhibitionClick = {},
        onCreateNewExhibitionConfirmed = {},
        onCreateNewExhibitionFabClicked = {},
        onDismissCreateExhibitionDialog = {},
        onDismissDeleteExhibitionDialog = {  },
        onShowDeleteExhibitionDialog = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ExhibitionScreenNoUserPreview() {
    ExhibitionScreenNoUser(onSignInClick = {})
}