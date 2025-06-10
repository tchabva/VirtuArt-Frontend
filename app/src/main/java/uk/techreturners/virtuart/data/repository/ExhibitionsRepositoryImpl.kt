package uk.techreturners.virtuart.data.repository

import android.util.Log
import uk.techreturners.virtuart.data.model.AddArtworkRequest
import uk.techreturners.virtuart.data.model.CreateExhibitionRequest
import uk.techreturners.virtuart.data.model.Exhibition
import uk.techreturners.virtuart.data.model.ExhibitionDetail
import uk.techreturners.virtuart.data.model.UpdateExhibitionRequest
import uk.techreturners.virtuart.data.remote.ExhibitionsApi
import uk.techreturners.virtuart.data.remote.NetworkResponse
import javax.inject.Inject

class ExhibitionsRepositoryImpl @Inject constructor(
    private val api: ExhibitionsApi
) : ExhibitionsRepository {

    override suspend fun getAllUserExhibitions(): NetworkResponse<List<Exhibition>> {
        try {
            val response = api.getAllUserExhibitions()
            val responseCode = response.code()

            if (responseCode == 200) {
                Log.i(
                    TAG,
                    "Successful Retrieval of Exhibitions: ${response.body()?.size} Exhibitions"
                )
                return NetworkResponse.Success(response.body()!!)
            } else {
                // TODO add code for expired token response
                Log.e(TAG, "Failed Retrieval of Albums: Code = $responseCode")
                return NetworkResponse.Failed(
                    response.message() ?: "",
                    code = responseCode,
                )
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun createExhibition(request: CreateExhibitionRequest): NetworkResponse<Exhibition> {
        try {
            val response = api.createExhibition(request)
            val responseCode = response.code()

            return if (responseCode == 201) {
                Log.i(TAG, "Successfully Created Exhibition: ${response.body()}")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed To Create Exhibition: Code = $responseCode")
                NetworkResponse.Failed(
                    response.message() ?: "",
                    code = responseCode,
                )
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun deleteExhibition(exhibitionId: String): NetworkResponse<Unit> {
        try {
            val response = api.deleteExhibition(exhibitionId)
            val responseCode = response.code()
            return if (responseCode == 204) {
                Log.i(TAG, "Successfully Deleted Exhibition ID: $exhibitionId")
                NetworkResponse.Success(Unit)
            } else {
                Log.e(TAG, "Failed To Delete Exhibition: Code = $responseCode")
                NetworkResponse.Failed(
                    response.message() ?: "",
                    code = responseCode,
                )
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun getExhibitionDetail(exhibitionId: String): NetworkResponse<ExhibitionDetail> {
        try {
            val response = api.getExhibitionDetail(exhibitionId)
            val responseCode = response.code()
            return if (responseCode == 200) {
                Log.i(TAG, "Successfully Retrieved Exhibition ID: $exhibitionId")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed To Retrieve Exhibition: Code = $responseCode")
                NetworkResponse.Failed(
                    response.message() ?: "",
                    code = responseCode,
                )
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun addArtworkToExhibition(
        exhibitionId: String,
        request: AddArtworkRequest
    ): NetworkResponse<Exhibition> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteArtworkFromExhibition(
        exhibitionId: String,
        apiId: String,
        source: String
    ): NetworkResponse<Unit> {
        try {
            val response = api.deleteArtworkFromExhibition(
                exhibitionId = exhibitionId,
                apiId = apiId,
                source = source
            )
            val responseCode = response.code()
            return if (responseCode == 204) {
                Log.i(TAG, "Successfully Deleted Artwork $apiId from Exhibition ID: $exhibitionId")
                NetworkResponse.Success(Unit)
            } else {
                Log.e(TAG, "Failed To Delete Artwork from Exhibition: Code = $responseCode")
                NetworkResponse.Failed(
                    response.message() ?: "",
                    code = responseCode,
                )
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun updateExhibitionDetails(
        exhibitionId: String,
        request: UpdateExhibitionRequest
    ): NetworkResponse<Exhibition> {
        try {
            val response = api.updateExhibitionDetails(
                exhibitionId = exhibitionId,
                updateExhibitionRequest = request
            )
            val responseCode = response.code()

            return if (responseCode == 200) {
                Log.i(TAG, "Successfully updated Exhibition: ${response.body()}")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed To updated Exhibition: Code = $responseCode")
                NetworkResponse.Failed(
                    response.message() ?: "",
                    code = responseCode,
                )
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    companion object {
        private const val TAG = "ExhibitionsRepoImpl"
    }
}