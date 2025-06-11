package uk.techreturners.virtuart.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uk.techreturners.virtuart.data.model.AicApiElasticSearchQuery
import uk.techreturners.virtuart.data.model.Artwork
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

    @POST("artworks/search/aic")
    suspend fun searchAicApi(@Body searchQuery: AicApiElasticSearchQuery): Response<PaginatedArtworkResults>
}