package uk.techreturners.virtuart.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.remote.ArtworksApi
import uk.techreturners.virtuart.data.remote.NetworkResponse
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class ArtworkPagingSource(
    private val artworksApi: ArtworksApi,
) : PagingSource<Int, ArtworkResult>() {

    // Called by the Paging Library asynchronously to fetch more data as the user scrolls
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtworkResult> {
        return try {
            // Starting page of 1 if no key is provided
            val page = params.key ?: STARTING_PAGE_INDEX
            val response = artworksApi.getAicArtworks(
                limit = params.loadSize.toString(),
                page = page.toString()
            )

            val artworks = response.body()?.data ?: emptyList()
            LoadResult.Page(
                data = artworks,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (artworks.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            // IOException for network failures.
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load()
    override fun getRefreshKey(state: PagingState<Int, ArtworkResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}