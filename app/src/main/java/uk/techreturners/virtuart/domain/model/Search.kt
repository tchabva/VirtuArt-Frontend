package uk.techreturners.virtuart.domain.model

data class AicAdvancedSearchQuery(
    val title: String? = null,
    val artist: String? = null,
    val medium: String? = null,
    val category: String? = null,
    val sortBy: String = "title.keyword", // default sort By
    val sortOrder: String = "asc", // default sort Order
    val limit: Int = 20,
    val source: String? = null,
    val page: Int = 1
)

data class BasicQuery(
    val query: String,
    val limit: String?,
    val source: String
)