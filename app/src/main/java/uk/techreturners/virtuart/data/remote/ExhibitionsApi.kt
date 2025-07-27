package uk.techreturners.virtuart.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import uk.techreturners.virtuart.data.model.AddArtworkRequest
import uk.techreturners.virtuart.data.model.CreateExhibitionRequest
import uk.techreturners.virtuart.data.model.Exhibition
import uk.techreturners.virtuart.data.model.ExhibitionDetail
import uk.techreturners.virtuart.data.model.UpdateExhibitionRequest

interface ExhibitionsApi {
    @GET("exhibitions")
    suspend fun getAllUserExhibitions(): Response<List<Exhibition>>

    @POST("exhibitions")
    suspend fun createExhibition(@Body createExhibitionRequest: CreateExhibitionRequest):
            Response<Exhibition>

    @DELETE("exhibitions/{id}")
    suspend fun deleteExhibition(@Path("id") exhibitionId: String): Response<Void>

    @GET("exhibitions/{id}")
    suspend fun getExhibitionDetail(@Path("id") exhibitionId: String): Response<ExhibitionDetail>

    @POST("exhibitions/{id}")
    suspend fun addArtworkToExhibition(
        @Path("id") exhibitionId: String,
        @Body addArtworkRequest: AddArtworkRequest
    ): Response<Exhibition>

    @DELETE("exhibitions/{id}/{apiId}/{source}")
    suspend fun deleteArtworkFromExhibition(
        @Path("id") exhibitionId: String,
        @Path("apiId") apiId: String,
        @Path("source") source: String
    ): Response<Void>

    @PATCH("exhibitions/{id}")
    suspend fun updateExhibitionDetails(
        @Path("id") exhibitionId: String,
        @Body updateExhibitionRequest: UpdateExhibitionRequest
    ): Response<Exhibition>
}