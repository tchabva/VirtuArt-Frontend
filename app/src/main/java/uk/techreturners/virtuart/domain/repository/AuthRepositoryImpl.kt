package uk.techreturners.virtuart.domain.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.ClearCredentialStateRequest.Companion.TYPE_CLEAR_CREDENTIAL_STATE
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.domain.model.SignInResult
import uk.techreturners.virtuart.domain.model.UserData
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager
) : AuthRepository {

    private var currentUser: UserData? = null

    override suspend fun signIn(): SignInResult {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            if (credential is GoogleIdTokenCredential) {
                val userData = UserData(
                    userId = credential.idToken,
                    email = credential.id,
                    displayName = credential.displayName,
                    profilePicture = credential.profilePictureUri?.toString()
                )
                currentUser = userData
                SignInResult.Success(userData)
            } else {
                SignInResult.Error(
                    "Sign In Error"
                )
            }
        } catch (e: GetCredentialException) {
            SignInResult.Error(e.message ?: "Unknown Error")
        }
    }

    override suspend fun signOut() {
        currentUser = null
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest(
                TYPE_CLEAR_CREDENTIAL_STATE
            )
        )
    }

    override fun getSignedInUser(): UserData? {
        return currentUser
    }
}