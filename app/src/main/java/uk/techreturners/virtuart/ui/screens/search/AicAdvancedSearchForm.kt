package uk.techreturners.virtuart.ui.screens.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import uk.techreturners.virtuart.domain.model.AicAdvancedSearchQuery

@Composable
internal fun AicAdvancedSearchForm(
    state: SearchViewModel.State.Search,
    onTitleChange: (String) -> Unit,
    onArtistChange: (String) -> Unit,
    onMediumChange: (String) -> Unit,
    onDepartmentChange: (String) -> Unit,
    onSortByChange: (String) -> Unit,
    onSortOrderChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.advanced_search_aic_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title TextField
            OutlinedTextField(
                value = state.advancedSearchQuery.title ?: "",
                maxLines = 1,
                onValueChange = onTitleChange,
                label = { Text(stringResource(R.string.title)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Artist TextField
            OutlinedTextField(
                value = state.advancedSearchQuery.artist ?: "",
                maxLines = 1,
                onValueChange = onArtistChange,
                label = { Text(stringResource(R.string.artist)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Medium TextField
            OutlinedTextField(
                value = state.advancedSearchQuery.medium ?: "",
                onValueChange = onMediumChange,
                maxLines = 1,
                label = { Text(stringResource(R.string.medium)) },
                modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category/Department TextField
            OutlinedTextField(
                value = state.advancedSearchQuery.department ?: "",
                onValueChange = onDepartmentChange,
                label = { Text(stringResource(R.string.department)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sort Options
            Text(
                text = stringResource(R.string.sort_by),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = when (state.advancedSearchQuery.sortBy) {
                            "title.keyword" -> "Title"
                            "date_end" -> "Date"
                            "artist_title.keyword" -> "Artist"
                            else -> "Title"
                        },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf(
                            "title.keyword" to "Title",
                            "date_end" to "Date",
                            "artist_title.keyword" to "Artist"
                        ).forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    onSortByChange(value)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // TODO Add to View Model
                var orderExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = orderExpanded,
                    onExpandedChange = { orderExpanded = !orderExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = if (state.advancedSearchQuery.sortOrder == "asc") "Ascending" else "Descending",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = orderExpanded) },
                        modifier = Modifier.menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = orderExpanded,
                        onDismissRequest = { orderExpanded = false }
                    ) {
                        listOf(
                            "asc" to "Ascending",
                            "desc" to "Descending"
                        ).forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    onSortOrderChange(value)
                                    orderExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search Button
                Button(
                    onClick = onSearch,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    enabled = !state.isSearching && (
                            !state.advancedSearchQuery.title.isNullOrBlank() ||
                                    !state.advancedSearchQuery.artist.isNullOrBlank() ||
                                    !state.advancedSearchQuery.medium.isNullOrBlank() ||
                                    !state.advancedSearchQuery.department.isNullOrBlank()
                            )
                ) {
                    Text(text = stringResource(R.string.search))
                }

                // Clear Button
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
                ) {
                    Text(
                        text = stringResource(R.string.clear),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AicAdvancedSearchFormPreview() {
    AicAdvancedSearchForm(
        state = SearchViewModel.State.Search(
            data = null,
            advancedSearchQuery = AicAdvancedSearchQuery(
                title = "name",
            )
        ),
        onTitleChange = {},
        onArtistChange = {},
        onMediumChange = {},
        onDepartmentChange = {},
        onSortByChange = {},
        onSortOrderChange = {},
        onSearch = {},
        onClear = {}
    )
}