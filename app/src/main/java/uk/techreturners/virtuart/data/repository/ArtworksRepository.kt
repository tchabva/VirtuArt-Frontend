package uk.techreturners.virtuart.data.repository

import retrofit2.http.Path
import uk.techreturners.virtuart.data.model.AicApiElasticSearchQuery
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.NetworkResponse

interface ArtworksRepository {
    suspend fun getAicArtworks(
        limit: String?,
        page: String?
    ): NetworkResponse<PaginatedArtworkResults>

    suspend fun getArtworksById(source: String, artworkId: String): NetworkResponse<Artwork>
    suspend fun searchAicApi(searchQuery: AicApiElasticSearchQuery): NetworkResponse<PaginatedArtworkResults>
}