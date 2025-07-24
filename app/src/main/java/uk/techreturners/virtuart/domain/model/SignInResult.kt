package uk.techreturners.virtuart.domain.model

sealed interface SignInResult {
    data class Success(val userData: UserData) : SignInResult
    data class Error(val exception: String) : SignInResult
}