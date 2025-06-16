package uk.techreturners.virtuart.domain.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.ClearCredentialStateRequest.Companion.TYPE_CLEAR_CREDENTIAL_STATE
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.domain.model.SignInResult
import uk.techreturners.virtuart.domain.model.UserData
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager
) : AuthRepository {

    private val _userState: MutableStateFlow<UserData?> = MutableStateFlow(null)
    override val userState: StateFlow<UserData?> = _userState

    private val _source: MutableStateFlow<String> = MutableStateFlow("aic")
    override val source: StateFlow<String> = _source

    override suspend fun signIn(): SignInResult {
        try {
            val webClientId = context.getString(R.string.web_client_id)

            val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(webClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            Log.d(TAG, "Received credential type: ${credential::class.java.simpleName}")
            Log.d(TAG, "Credential type string: ${credential.type}")

            return if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val userData = UserData(
                    userId = googleIdTokenCredential.idToken,
                    email = googleIdTokenCredential.id,
                    displayName = googleIdTokenCredential.displayName,
                    profilePicture = googleIdTokenCredential.profilePictureUri?.toString()
                )

                Log.d(TAG, "Sign in successful for user: ${userData.email}")
                Log.d(TAG, "Sign in successful for user: ${userData.userId}")
                _userState.value = userData
                SignInResult.Success(userData)
            }else {
                Log.e(TAG, "Unexpected credential type: ${credential.type}")
                SignInResult.Error("Failed to process Google credential")
            }
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException: ${e.message}", e)
            return SignInResult.Error("Authentication failed: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during sign in", e)
            return SignInResult.Error("Unexpected error: ${e.message}")
        }
    }

    override suspend fun signOut() {
        _userState.value = null
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest(
                TYPE_CLEAR_CREDENTIAL_STATE
            )
        )
    }

    override fun getSignedInUser(): UserData? {
        return userState.value
    }

    override fun updateSource(newSource: String) {
        _source.value = newSource
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}