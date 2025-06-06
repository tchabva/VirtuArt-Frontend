package uk.techreturners.virtuart.domain.repository

import androidx.credentials.exceptions.GetCredentialException
import uk.techreturners.virtuart.domain.model.UserData

sealed interface SignInResult {
    data class Success(val userData: UserData) : SignInResult
    data class Error(val exception: GetCredentialException) : SignInResult
}