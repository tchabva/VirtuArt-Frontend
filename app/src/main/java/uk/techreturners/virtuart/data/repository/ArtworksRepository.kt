package uk.techreturners.virtuart.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uk.techreturners.virtuart.data.model.AicApiElasticSearchQuery
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.NetworkResponse

interface ArtworksRepository {
    fun getArtworks(): Flow<PagingData<ArtworkResult>>
    suspend fun getArtworksById(source: String, artworkId: String): NetworkResponse<Artwork>
    suspend fun searchAicApi(searchQuery: AicApiElasticSearchQuery): NetworkResponse<PaginatedArtworkResults>
}