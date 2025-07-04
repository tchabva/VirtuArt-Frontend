package uk.techreturners.virtuart.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults

interface SearchApi {
    // TODO consider making a Search Api Repo and controller
    @POST("artworks/search")
    suspend fun searchApiBasic(@Body searchQuery: BasicSearchQuery): Response<PaginatedArtworkResults>

    @POST("artworks/search/advanced")
    suspend fun searchApiAdvanced(@Body searchQuery: AdvancedSearchRequest): Response<PaginatedArtworkResults>
}