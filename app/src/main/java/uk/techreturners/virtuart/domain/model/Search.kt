package uk.techreturners.virtuart.domain.model

data class AicAdvancedSearchQuery(
    val title: String? = null,
    val artist: String? = null,
    val medium: String? = null,
    val department: String? = null,
    val sortBy: String = "title.keyword", // default sort By
    val sortOrder: String = "asc", // default sort Order
)

data class BasicQuery(
    val query: String? = null,
    val source: String? = null
)