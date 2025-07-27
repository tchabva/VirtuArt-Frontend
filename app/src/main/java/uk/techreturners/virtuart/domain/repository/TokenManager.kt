package uk.techreturners.virtuart.domain.repository

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.techreturners.virtuart.R
import javax.inject.Inject

class TokenManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val credentialManager: CredentialManager,
) {
    suspend fun tokenRefresh(): Boolean {
        val currentUser = authRepository.getSignedInUser()

        if (currentUser?.userId == null) {
            Log.e(TAG, "No current user found for token refresh")
            return false
        }

        return try {
            Log.i(TAG, "Attempting to refresh expired token")
            val refreshed = attemptTokenRefresh(filterByAuthorizedAccounts = true)

            if (refreshed) {
                Log.i(TAG, "Silent token refresh successful")
                return true
            }

            Log.w(TAG, "Silent refresh failed, attempting interactive refresh")
            val interactiveRefresh = attemptTokenRefresh(filterByAuthorizedAccounts = false)

            if (interactiveRefresh) {
                Log.i(TAG, "Interactive token refresh successful")
                true
            }

            Log.e(TAG, "Both silent & interactive token refresh failed")
            false
        } catch (e: Exception) {
            Log.e(TAG, "Token refresh failed, user signed out", e)
            authRepository.signOut()
            false
        }
    }

    private suspend fun attemptTokenRefresh(filterByAuthorizedAccounts: Boolean): Boolean {
        return try {
            val webClientId = context.getString(R.string.web_client_id)

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                // Update the stored user data with fresh token
                val currentUser = authRepository.getSignedInUser()!!
                val updatedUserData = currentUser.copy(
                    userId = googleIdTokenCredential.idToken
                )

                authRepository.updateUserData(updatedUserData)
                Log.i(TAG, "Token refreshed successfully")
                true
            } else {
                Log.e(TAG, "Unexpected credential type: ${credential.type}")
                false
            }
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException during token refresh: ${e.message}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "Exception during token refresh: ${e.message}")
            false
        }
    }

    companion object {
        private const val TAG = "TokenManager"
    }
}