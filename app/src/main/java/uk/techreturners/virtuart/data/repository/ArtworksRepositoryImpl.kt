package uk.techreturners.virtuart.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uk.techreturners.virtuart.data.model.Artwork
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.paging.ArtworkPagingSource
import uk.techreturners.virtuart.data.remote.ArtworksApi
import uk.techreturners.virtuart.data.remote.NetworkResponse
import javax.inject.Inject

class ArtworksRepositoryImpl @Inject constructor(
    private val api: ArtworksApi
) : ArtworksRepository {

    override fun getArtworks(source: String): Flow<PagingData<ArtworkResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArtworkPagingSource(artworksApi = api, source = source) }
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

    companion object {
        private const val TAG = "ArtworksRepoImpl"
    }
}