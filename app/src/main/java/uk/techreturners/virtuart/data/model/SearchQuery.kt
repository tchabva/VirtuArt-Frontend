package uk.techreturners.virtuart.data.model

data class AicApiElasticSearchQuery(
    val query: Map<String, Any>,
    val sort: List<Map<String, Any>>,
    val size: Int,
    val page: Int
)

data class BasicSearchQuery(
    val q: String,
    val query: Map<String,Any>
)