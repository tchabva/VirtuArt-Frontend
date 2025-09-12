package uk.techreturners.virtuart.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.ui.common.DefaultSearchButton

@Composable
internal fun SimpleSearchForm(
    state: SearchViewModel.State.Search,
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onClear: () -> Unit = {}
) {
    Column {
        OutlinedTextField(
            value = state.basicQuery.query ?: "",
            onValueChange = onQueryChange,
            label = { Text(stringResource(R.string.search_artworks_txt)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            },
            trailingIcon = {
                if (!state.basicQuery.query.isNullOrBlank()) {
                    IconButton(
                        onClick = onClear
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions().copy(
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Search Button
        DefaultSearchButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSearch,
            isEnabled = !state.isSearching && !state.basicQuery.query.isNullOrBlank()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleSearchFormPreview() {
    SimpleSearchForm(
        state = SearchViewModel.State.Search(
            data = null,
            basicQuery = BasicSearchQuery(
                query = "Monet"
            ),
            source = "aic"
        ),
    )
}