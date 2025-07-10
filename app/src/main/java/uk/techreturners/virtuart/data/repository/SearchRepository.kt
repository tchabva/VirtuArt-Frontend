package uk.techreturners.virtuart.data.repository

import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.NetworkResponse

interface SearchRepository {
    suspend fun advancedApiSearch(searchQuery: AdvancedSearchRequest): NetworkResponse<PaginatedArtworkResults>
    suspend fun basicApiSearch(searchQuery: BasicSearchQuery): NetworkResponse<PaginatedArtworkResults>
}