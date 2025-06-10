package uk.techreturners.virtuart.data.model

data class AicApiElasticSearchQuery(
    val query: Query?,
    val sort: List<Map<String, Any>>?,
    val fields: List<String> = listOf("id", "title", "date_display", "artist_title", "image_id"),
    val size: Int,
    val page: Int
) {
    data class Query(
        val bool: BoolQuery?
    )

    data class BoolQuery(
        val must: List<Map<String, Any>>?
    )
}