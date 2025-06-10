package uk.techreturners.virtuart.data.repository

import android.util.Log
import uk.techreturners.virtuart.data.model.AicApiElasticSearchQuery
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.ArtworksApi
import uk.techreturners.virtuart.data.remote.NetworkResponse
import javax.inject.Inject

class ArtworksRepositoryImpl @Inject constructor(
    private val api: ArtworksApi
) : ArtworksRepository {

    override suspend fun getAicArtworks(
        limit: String?,
        page: String?
    ): NetworkResponse<PaginatedArtworkResults> {
        try {
            val response = api.getAicArtworks(limit = limit, page = page)
            val responseCode = response.code()

            return if (responseCode == 200) {
                Log.i(TAG, "Successful retrieval of artworks: ${response.body()}")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed retrieval of artworks: Code = $responseCode")
                NetworkResponse.Failed(
                    message = response.message(),
                    code = responseCode
                )
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun getArtworksById(
        source: String,
        artworkId: String
    ): NetworkResponse<Artwork> {
        try {
            val response = api.getArtworkById(source = source, artworkId = artworkId)
            val responseCode = response.code()

            return if (responseCode == 200) {
                Log.i(TAG, "Successful retrieval of artwork: ${response.body()}")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed retrieval of artwork: Code = $responseCode")
                NetworkResponse.Failed(message = response.message(), code = responseCode)
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun searchAicApi(searchQuery: AicApiElasticSearchQuery): NetworkResponse<PaginatedArtworkResults> {
        try {
            val response = api.searchAicApi(searchQuery = searchQuery)
            val responseCode = response.code()

            return if (responseCode == 200) {
                Log.i(TAG, "Successful search of artworks: ${response.body()}")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed search of artworks: Code = $responseCode")
                NetworkResponse.Failed(message = response.message(), code = responseCode)
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    companion object {
        private const val TAG = "ArtworksRepoImpl"
    }
}