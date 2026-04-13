package uk.techreturners.virtuart.data.paging

import uk.techreturners.virtuart.data.model.AdvancedSearchRequest

sealed class SearchPagingParams {
    abstract val source: String
    abstract val pageSize: Int

    data class Basic(
        val query: String,
        override val source: String,
        override val pageSize: Int,
    ) : SearchPagingParams()

    data class Advanced(
        val request: AdvancedSearchRequest,
    ) : SearchPagingParams() {
        override val source: String get() = request.source!!
        override val pageSize: Int get() = request.pageSize
    }
}
