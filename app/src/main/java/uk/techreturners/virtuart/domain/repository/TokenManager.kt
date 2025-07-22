package uk.techreturners.virtuart.domain.repository

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
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

    suspend fun validateAndRefreshToken(): Boolean {
        val currentUser = authRepository.getSignedInUser()

        if (currentUser?.userId == null) {
            return false
        }

        return try {
            // Attempt to get fresh credentials silently
            val webClientId = context.getString(R.string.web_client_id)

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
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
                val updatedUserData = currentUser.copy(
                    userId = googleIdTokenCredential.idToken
                )

                authRepository.updateUserData(updatedUserData)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Token refresh failed", e)
            false
        }
    }

    companion object {
        private const val TAG = "TokenManager"
    }
}