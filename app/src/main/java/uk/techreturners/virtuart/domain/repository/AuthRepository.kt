package uk.techreturners.virtuart.domain.repository

import uk.techreturners.virtuart.domain.model.SignInResult
import uk.techreturners.virtuart.domain.model.UserData

interface AuthRepository {
    suspend fun signIn(): SignInResult
    fun signOut()
    fun getSignedInUser(): UserData?
}