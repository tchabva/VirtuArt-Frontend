package uk.techreturners.virtuart.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.domain.model.UserData
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Welcome to VirtuArt Exhibition Curator\nSign in to view your profile and create Exhibitions",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onSignIn() }) {
                        Text("Sign in with Google")
                    }
                }
            }
        }

        is ProfileViewModel.State.SignedIn -> {
            ProfileScreenSignedIn(
                state = state,
                signOut = { onSignOut() }
            )
        }
    }
}

@Composable
private fun ProfileScreenSignedIn(
    state: ProfileViewModel.State.SignedIn,
    signOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile_screen_heading),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Profile Picture
        GlideImage(
            model = state.currentUser?.profilePicture,
            contentDescription = stringResource(R.string.user_profile_picture_description),
            loading = placeholder(R.drawable.ic_launcher_foreground),
            failure = placeholder(R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User info cards
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = stringResource(R.string.name),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.currentUser?.displayName ?: stringResource(R.string.unknown),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.currentUser?.email ?: stringResource(R.string.unknown),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = { signOut() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(R.string.sign_out_btn_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenSignedInPreview() {
    ProfileScreenSignedIn(
        state = ProfileViewModel.State.SignedIn(
            currentUser = UserData(
                userId = "id",
                displayName = "John Doe",
                email = "john.doe@example.com",
                profilePicture = "/placeholder.svg?height=120&width=120"
            )
        ),
        signOut = {}
    )
}