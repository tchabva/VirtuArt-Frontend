package uk.techreturners.virtuart.ui.screens.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    signInButtonClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
){
    Text(
        modifier = Modifier.padding(16.dp),
        text = "Profile Screen"
    )
}