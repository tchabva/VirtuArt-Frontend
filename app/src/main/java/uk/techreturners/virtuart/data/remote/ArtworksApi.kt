package uk.techreturners.virtuart.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults

interface ArtworksApi {
    @GET("artworks/aic")
    suspend fun getAicArtworks(
        @Query("limit") limit: String?,
        @Query("page") page: String?
    ): Response<PaginatedArtworkResults>

    @GET("artworks/{source}/{id}")
    suspend fun getArtworkById(
        @Path("source") source: String,
        @Path("id") artworkId: String
    ): Response<Artwork>

    // TODO consider making a Search Api Repo and controller
    @POST("artworks/search")
    suspend fun searchApiBasic(@Body searchQuery: BasicSearchQuery): Response<PaginatedArtworkResults>

    @POST("artworks/search/advanced")
    suspend fun searchApiAdvanced(@Body searchQuery: AdvancedSearchRequest): Response<PaginatedArtworkResults>
}