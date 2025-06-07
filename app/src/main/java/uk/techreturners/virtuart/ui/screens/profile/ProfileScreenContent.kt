package uk.techreturners.virtuart.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator


@Composable
fun ProfileScreenContent(
    state: ProfileViewModel.State,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {

    when (state) {
        is ProfileViewModel.State.Error -> {
            DefaultErrorScreen(
                responseCode = null,
                errorMessage = state.errorMessage
            )
        }
        ProfileViewModel.State.Loading -> {
            DefaultProgressIndicator()
        }
        ProfileViewModel.State.NoUser -> {

            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Welcome to VirtuArt Exhibition Curator\nSign in to view your profile and create Exhibitions", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onSignIn() }) {
                        Text("Sign in with Google")
                    }
                }
            }
        }
        is ProfileViewModel.State.SignedIn -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlideImage(
                        model = state.currentUser?.profilePicture,
                        contentDescription = "Album Artwork",
                        loading = placeholder(R.drawable.ic_launcher_foreground),
                        failure = placeholder(R.drawable.ic_launcher_foreground),
                        modifier = Modifier
                            .padding(8.dp)
                            .size(120.dp)
                    )
                    Text("Name: ${state.currentUser?.displayName}", style = MaterialTheme.typography.headlineMedium)
                    Text("Id: ${state.currentUser?.userId}", style = MaterialTheme.typography.headlineMedium)
                    Text("email: ${state.currentUser?.email}", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onSignOut()}) {
                        Text("Signout")
                    }
                }
            }
        }
    }
}