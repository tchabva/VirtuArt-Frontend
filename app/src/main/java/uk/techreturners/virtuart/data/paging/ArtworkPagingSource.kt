package uk.techreturners.virtuart.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.remote.NetworkResponse
import uk.techreturners.virtuart.data.repository.ArtworksRepository

private const val STARTING_PAGE_INDEX = 1

class ArtworkPagingSource(
    private val repository: ArtworksRepository,
) : PagingSource<Int, ArtworkResult>() {

    // Called by the Paging Library asynchronously to fetch more data as the user scrolls
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtworkResult> {
        return try {
            // Starting page of 1 if no key is provided
            val page = params.key ?: STARTING_PAGE_INDEX
            val networkResponse = repository.getAicArtworks(
                limit = params.loadSize.toString(),
                page = page.toString()
            )

            when (networkResponse) {
                is NetworkResponse.Exception -> {
                    LoadResult.Error(networkResponse.exception)
                }

                is NetworkResponse.Failed -> {
                    LoadResult.Error(
                        Exception("Failed to load articles: \ncode: ${networkResponse.code}\n${networkResponse.message}")
                    )
                }

                is NetworkResponse.Success -> {
                    val artworks = networkResponse.data.data
                    LoadResult.Page(
                        data = artworks,
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = if (artworks.isEmpty()) null else page + 1
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
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