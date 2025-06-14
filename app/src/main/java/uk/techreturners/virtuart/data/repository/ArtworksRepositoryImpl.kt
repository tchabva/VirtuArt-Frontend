package uk.techreturners.virtuart.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uk.techreturners.virtuart.data.model.AdvancedSearchRequest
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.paging.ArtworkPagingSource
import uk.techreturners.virtuart.data.remote.ArtworksApi
import uk.techreturners.virtuart.data.remote.NetworkResponse
import javax.inject.Inject

class ArtworksRepositoryImpl @Inject constructor(
    private val api: ArtworksApi
) : ArtworksRepository {

    override fun getArtworks(): Flow<PagingData<ArtworkResult>> {
       return Pager(
           config = PagingConfig(
               pageSize = 20,
               enablePlaceholders = false
           ),
           pagingSourceFactory = { ArtworkPagingSource(api)}
       ).flow
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
        private const val TAG = "ArtworksRepoImpl"
    }
}