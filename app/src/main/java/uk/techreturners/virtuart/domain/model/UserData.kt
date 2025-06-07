package uk.techreturners.virtuart.domain.model

data class UserData(
    val userId: String,
    val email: String?,
    val displayName: String?,
    val profilePicture: String?
)