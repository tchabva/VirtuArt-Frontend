package uk.techreturners.virtuart.data.model

data class AdvancedSearchRequest(
    val title: String? = null,
    val artist: String? = null,
    val medium: String? = null,
    val department: String? = null,
    val sortBy: String = "Title", // default sort By
    val sortOrder: String = "Ascending", // default sort Order
    val source: String? = null,
    val pageSize: Int = 20,
    val currentPage: Int = 1
)

data class BasicSearchQuery(
    val query: String? = null,
    val source: String? = null,
    val pageSize: Int = 20,
    val currentPage: Int = 1
)