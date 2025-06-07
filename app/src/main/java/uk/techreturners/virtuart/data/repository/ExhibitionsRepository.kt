package uk.techreturners.virtuart.data.repository

import uk.techreturners.virtuart.data.model.AddArtworkRequest
import uk.techreturners.virtuart.data.model.CreateExhibitionRequest
import uk.techreturners.virtuart.data.model.Exhibition
import uk.techreturners.virtuart.data.model.ExhibitionDetail
import uk.techreturners.virtuart.data.model.UpdateExhibitionRequest
import uk.techreturners.virtuart.data.remote.NetworkResponse

interface ExhibitionsRepository {
    suspend fun getAllUserExhibitions(): NetworkResponse<List<Exhibition>>
    suspend fun createExhibition(request: CreateExhibitionRequest): NetworkResponse<Exhibition>
    suspend fun deleteExhibition(exhibitionId: String): NetworkResponse<Void>
    suspend fun getExhibitionDetail(exhibitionId: String): NetworkResponse<ExhibitionDetail>
    suspend fun addArtworkToExhibition(
        exhibitionId: String,
        request: AddArtworkRequest
    ): NetworkResponse<Exhibition>

    suspend fun deleteArtworkFromExhibition(
        exhibitionId: String,
        apiId: String,
        source: String
    ): NetworkResponse<Void>

    suspend fun updateExhibitionDetails(
        exhibitionId: String,
        request: UpdateExhibitionRequest
    ): NetworkResponse<Exhibition>
}