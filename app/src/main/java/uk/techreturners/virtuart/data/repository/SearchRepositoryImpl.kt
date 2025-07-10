package uk.techreturners.virtuart.data.repository

import android.util.Log
import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.remote.SearchApi
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi
) : SearchRepository {
    override suspend fun advancedApiSearch(searchQuery: AdvancedSearchRequest): NetworkResponse<PaginatedArtworkResults> {
        try {
            val response = api.searchApiAdvanced(searchQuery = searchQuery)
            val responseCode = response.code()

            return if (responseCode == 200) {
                Log.i(TAG, "Successful advanced search of artworks: ${response.body()}")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed advanced search of artworks: Code = $responseCode")
                NetworkResponse.Failed(message = response.message(), code = responseCode)
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    override suspend fun basicApiSearch(searchQuery: BasicSearchQuery): NetworkResponse<PaginatedArtworkResults> {
        try {
            val response = api.searchApiBasic(searchQuery = searchQuery)
            val responseCode = response.code()

            return if (responseCode == 200) {
                Log.i(TAG, "Successful basic search of artworks: ${response.body()}")
                NetworkResponse.Success(response.body()!!)
            } else {
                Log.e(TAG, "Failed basic search of artworks: Code = $responseCode")
                NetworkResponse.Failed(message = response.message(), code = responseCode)
            }
        } catch (e: Throwable) {
            Log.wtf(TAG, "Network Error", e)
            return NetworkResponse.Exception(e)
        }
    }

    companion object {
        private const val TAG = "SearchRepoImpl"
    }
}