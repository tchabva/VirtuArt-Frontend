package uk.techreturners.virtuart.domain.model

data class AicAdvancedSearchQuery(
    val title: String?,
    val artist: String?,
    val medium: String?,
    val category: String?,
    val relevance: String?,
    val sort: String?,
    val limit: String?,
    val source: String
)

data class BasicQuery(
    val query: String,
    val limit: String?,
    val source: String
)