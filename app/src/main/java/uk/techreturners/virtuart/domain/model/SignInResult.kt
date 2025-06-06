package uk.techreturners.virtuart.domain.model

import androidx.credentials.exceptions.GetCredentialException

sealed interface SignInResult {
    data class Success(val userData: UserData) : SignInResult
    data class Error(val exception: GetCredentialException) : SignInResult
}