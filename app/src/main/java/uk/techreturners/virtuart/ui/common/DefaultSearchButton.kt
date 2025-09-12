package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R

@Composable
fun DefaultSearchButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = isEnabled
    ) {
        Text(text = stringResource(R.string.search))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEnabledDefaultSearchButton(){
    DefaultSearchButton(
        isEnabled = true,
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewDisabledDefaultSearchButton(){
    DefaultSearchButton(
        isEnabled = false,
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    )
}