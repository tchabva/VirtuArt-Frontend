package uk.techreturners.virtuart.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import uk.techreturners.virtuart.R

@Composable
fun DefaultErrorScreen(responseCode: Int?, errorMessage: String?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            text = stringResource(
                R.string.error_occurred,
                responseCode ?: "N/A",
                errorMessage ?: stringResource(R.string.unknown_error)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultErrorScreenPreview() {
    DefaultErrorScreen(
        responseCode = 404,
        errorMessage = ""
    )
}