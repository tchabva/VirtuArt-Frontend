package uk.techreturners.virtuart.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R

@Composable
fun PageSizeDialog(
    onDismiss: () -> Unit = {},
    onPageSizeChanged: (Int) -> Unit = {},
    state: SearchViewModel.State.Search
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Items per page") },
        text = {
            Column {
                listOf(10, 20, 50).forEach { size ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.pageSize == size,
                            onClick = {
                                onPageSizeChanged(size)
                                onDismiss()
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$size items per page",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
            ) {
                Text(
                    text = stringResource(R.string.dismiss),
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
fun PageSizeDialogPreview() {
    PageSizeDialog(
        state = SearchViewModel.State.Search(
            pageSize = 10,
            source = "cma"
        )
    )
}