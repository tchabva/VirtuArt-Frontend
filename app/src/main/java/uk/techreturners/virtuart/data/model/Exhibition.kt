package uk.techreturners.virtuart.data.model

import java.time.LocalDateTime

data class Exhibition(
    val id: String,
    val title: String,
    val itemCount: Int,
    val createdAt: LocalDateTime,
    val updateAt: LocalDateTime
)

data class ExhibitionDetail(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime,
    val updateAt: LocalDateTime,
    val exhibitionItems: List<ExhibitionItem>
)

data class ExhibitionItem(
    val id: String,
    val apiId: String,
    val title: String,
    val date: String,
    val imageUrl: String,
    val source: String
)

data class CreateExhibitionRequest(
    val title: String,
    val description: String
)

data class UpdateExhibitionRequest(
    val title: String?,
    val description: String?
)

data class AddArtworkRequest(
    val apiId: String,
    val source: String
)