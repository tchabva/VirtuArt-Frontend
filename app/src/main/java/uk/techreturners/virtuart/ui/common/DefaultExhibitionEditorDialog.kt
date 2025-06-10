package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R

@Composable
fun DefaultExhibitionEditorDialog(
    exhibitionTitle: String,
    exhibitionDescription: String?,
    dialogTitle: String,
    confirmButtonText: String,
    onDismiss: () -> Unit,
    onCreateExhibitionRequestConfirmed: () -> Unit,
    updateTitle: (String) -> Unit,
    updateDescription: (String) -> Unit
) {
    var title by remember { mutableStateOf(exhibitionTitle) }
    var description by remember { mutableStateOf(exhibitionDescription) }

    AlertDialog(
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        updateTitle(it)
                    },
                    label = { Text(stringResource(R.string.title)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    singleLine = true

                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description ?: "",
                    onValueChange = {
                        description = it
                        updateDescription(it)
                    },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            OutlinedButton(
                onClick = onCreateExhibitionRequestConfirmed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text(
                    text = confirmButtonText,
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
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultExhibitionEditorDialogPreview() {
    DefaultExhibitionEditorDialog(
        onDismiss = {},
        onCreateExhibitionRequestConfirmed = {},
        exhibitionTitle = "",
        exhibitionDescription = "",
        dialogTitle = "Dialog Title",
        confirmButtonText = "Confirm Button",
        updateTitle = {},
        updateDescription = {},
    )
}