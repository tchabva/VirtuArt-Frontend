package uk.techreturners.virtuart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import uk.techreturners.virtuart.ui.navigation.NavRoot
import uk.techreturners.virtuart.ui.theme.VirtuArtTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirtuArtTheme {
                NavRoot()
            }
        }
    }
}