package uk.techreturners.virtuart.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uk.techreturners.virtuart.domain.model.SignInResult
import uk.techreturners.virtuart.domain.model.UserData

interface AuthRepository {
    val userState: StateFlow<UserData?>
    suspend fun signIn(): SignInResult
    suspend fun signOut()
    fun getSignedInUser(): UserData?
}