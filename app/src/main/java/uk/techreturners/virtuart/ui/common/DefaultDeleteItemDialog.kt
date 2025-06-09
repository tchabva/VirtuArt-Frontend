package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R

// The Dialog Alert for deleting items
@Composable
fun DefaultDeleteItemDialog(
    title: String,
    alertText: String,
    onDismiss: () -> Unit,
    onDeleteItemConfirmed: () -> Unit
) {

    AlertDialog(
        icon = { Icons.Default.Warning },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = alertText)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDeleteItemConfirmed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                Text(
                    text = stringResource(R.string.no),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultDeleteItemDialogPreview() {
    DefaultDeleteItemDialog(
        onDismiss = {},
        onDeleteItemConfirmed = {},
        title = stringResource(R.string.delete_exhibition),
        alertText = stringResource(R.string.delete_exhibition_alert_dialog_txt)
    )
}