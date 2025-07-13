package uk.techreturners.virtuart.data.model

data class Exhibition(
    val id: String,
    val title: String,
    val itemCount: Int,
    val createdAt: String,
    val updateAt: String
)

data class ExhibitionDetail(
    val id: String,
    val title: String,
    val description: String?,
    val createdAt: String,
    val updateAt: String?,
    val exhibitionItems: List<ExhibitionItem>
)

data class ExhibitionItem(
    val id: String,
    val apiId: String,
    val title: String?,
    val artist: String?,
    val date: String?,
    val imageUrl: String?,
    val source: String
)

data class CreateExhibitionRequest(
    val title: String,
    val description: String?
)

data class UpdateExhibitionRequest(
    val title: String?,
    val description: String?
)

data class AddArtworkRequest(
    val apiId: String,
    val source: String,
)