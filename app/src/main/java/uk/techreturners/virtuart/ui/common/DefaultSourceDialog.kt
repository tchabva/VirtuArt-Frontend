package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R

@Composable
fun DefaultSourceDialog(
    onDismiss: () -> Unit = {},
    onSourceChanged: (String) -> Unit = {},
    source: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_museum_source)) },
        text = {
            Column {
                listOf(
                    stringResource(R.string.aic) to stringResource(R.string.aic_full_name),
                    stringResource(R.string.cma) to stringResource(R.string.cma_full_name)
                ).forEach { (key, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .selectable(
                                selected = source == key,
                            ) { onSourceChanged(key) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier.clearAndSetSemantics { },
                            selected = source == key,
                            onClick = { onSourceChanged(key) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = value,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        confirmButton = {
            DefaultOutlinedButton(
                buttonText = stringResource(R.string.dismiss),
                onClick = onDismiss
            )
        }
    )
}

@Preview
@Composable
private fun DefaultSourceDialogPreview() {
    DefaultSourceDialog(source = "aic")
}