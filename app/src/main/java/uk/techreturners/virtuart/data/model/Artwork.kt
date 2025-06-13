package uk.techreturners.virtuart.data.model

data class Artwork(
    val id: String,
    val title: String,
    val artist: String?,
    val date: String?,
    val displayMedium: String?,
    val imageUrl: String?,
    val altImageUrls: List<String>,
    val description: String?,
    val origin: String?,
    val category: String?,
    val sourceMuseum: String,
)

data class ArtworkResult(
    val id: String,
    val title: String,
    val artistTitle: String?,
    val date: String?,
    val imageURL: String,
    val source: String
)

data class PaginatedArtworkResults(
    val totalItems: Int,
    val pageSize: Int,
    val totalPages: Int,
    val currentPage: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val data: List<ArtworkResult>,
)