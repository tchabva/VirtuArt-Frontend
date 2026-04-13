package uk.techreturners.virtuart.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import uk.techreturners.virtuart.data.model.ArtworkResult
import uk.techreturners.virtuart.data.model.BasicSearchQuery
import uk.techreturners.virtuart.data.model.PaginatedArtworkResults
import uk.techreturners.virtuart.data.remote.SearchApi
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class SearchPagingSource(
    private val api: SearchApi,
    private val params: SearchPagingParams,
    private val onInitialPageLoaded: (PaginatedArtworkResults) -> Unit,
) : PagingSource<Int, ArtworkResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtworkResult> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX
            val response = when (val p = this.params) {
                is SearchPagingParams.Basic -> api.searchApiBasic(
                    BasicSearchQuery(
                        query = p.query,
                        source = p.source,
                        pageSize = p.pageSize,
                        currentPage = page,
                    )
                )

                is SearchPagingParams.Advanced -> api.searchApiAdvanced(
                    p.request.copy(
                        source = p.source,
                        pageSize = p.pageSize,
                        currentPage = page,
                    )
                )
            }

            if (!response.isSuccessful) {
                Log.e(TAG, "Search failed: code=${response.code()}")
                return LoadResult.Error(HttpException(response))
            }

            val body = response.body()
                ?: return LoadResult.Error(IOException("Empty search response body"))

            if (page == STARTING_PAGE_INDEX) {
                onInitialPageLoaded(body)
            }

            LoadResult.Page(
                data = body.data,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (body.hasNext) page + 1 else null,
            )
        } catch (exception: IOException) {
            Log.e(TAG, "Exception: $exception", exception)
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e(TAG, "HttpException: ${exception.code()}", exception)
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArtworkResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val TAG = "SearchPagingSource"
    }
}
