package uk.techreturners.virtuart.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.paging.SearchPagingParams
import uk.techreturners.virtuart.data.remote.NetworkResponse

interface SearchRepository {
    suspend fun advancedApiSearch(searchQuery: AdvancedSearchRequest): NetworkResponse<PaginatedArtworkResults>
    suspend fun basicApiSearch(searchQuery: BasicSearchQuery): NetworkResponse<PaginatedArtworkResults>

    fun searchPaged(
        params: SearchPagingParams,
        onInitialPageLoaded: (PaginatedArtworkResults) -> Unit,
    ): Flow<PagingData<ArtworkResult>>
}