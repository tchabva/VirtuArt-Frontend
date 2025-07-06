package uk.techreturners.virtuart.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.domain.model.UserData
import uk.techreturners.virtuart.ui.common.DefaultErrorScreen
import uk.techreturners.virtuart.ui.common.DefaultProgressIndicator
import uk.techreturners.virtuart.ui.theme.VirtuArtTheme


@Composable
fun ProfileScreenContent(
    state: ProfileViewModel.State,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onTryAgainClicked: () -> Unit,
) {

    when (state) {
        is ProfileViewModel.State.Error -> {
            DefaultErrorScreen(
                buttonText = stringResource(R.string.try_again),
                onClick = { TODO() }
            )
        }

        ProfileViewModel.State.Loading -> {
            DefaultProgressIndicator()
        }

        ProfileViewModel.State.NoUser -> {
            ProfileScreenNoUser(
                onSignIn = onSignIn
            )
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
        // View Title
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

        // Sign Out button
        OutlinedButton(
            onClick = { signOut() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = stringResource(R.string.sign_out_btn_text),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfileScreenNoUser(
    onSignIn: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val logo = if (isDarkTheme) {
            painterResource(R.drawable.ic_launcher_foreground_white)
        } else {
            painterResource(R.drawable.ic_launcher_foreground)
        }

        // App Logo
        Image(
            painter = logo,
            contentDescription = stringResource(R.string.app_logo_description),
            modifier = Modifier
                .size(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Welcome Text
        Text(
            text = stringResource(R.string.login_page_welcome),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Instructions
        Text(
            text = stringResource(R.string.login_screen_instructions),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Google Sign In Button
        OutlinedButton(
            onClick = onSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = stringResource(R.string.google_logo),
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.google_sign_in_btn),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // New User Info Text
        Text(
            text = stringResource(R.string.login_page_new_user_instructions),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
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

@Preview(showBackground = true)
@Composable
private fun ProfileScreenNoUserPreview() {
    ProfileScreenNoUser(
        onSignIn = {}
    )
}