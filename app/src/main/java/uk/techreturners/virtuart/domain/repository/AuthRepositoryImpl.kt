package uk.techreturners.virtuart.domain.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.ClearCredentialStateRequest.Companion.TYPE_CLEAR_CREDENTIAL_STATE
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.domain.model.SignInResult
import uk.techreturners.virtuart.domain.model.UserData
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager,
    private val userPreferenceDataStore: DataStore<Preferences>
) : AuthRepository {

    private val _userState: MutableStateFlow<UserData?> = MutableStateFlow(null)
    override val userState: StateFlow<UserData?> = _userState

    private val _source: MutableStateFlow<String> = MutableStateFlow("aic")
    override val source: StateFlow<String> = _source

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val savedUser = getUserData()
            if(savedUser != null){
                _userState.value = savedUser
            }
        }
    }

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

                // Persist the UserData
                saveUserData(userData)

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
        clearUserData()
        Log.i(TAG, "User signed out")
    }

    override fun getSignedInUser(): UserData? {
        Log.i(TAG, "Get getSignedInUser method invoked")
        return userState.value
    }

    override fun updateSource(newSource: String) {
        _source.value = newSource
        Log.i(TAG, "Update the source value: ${source.value}")
    }

    override suspend fun updateUserData(userData: UserData) {
        saveUserData(userData)
    }

    private suspend fun saveUserData(userData: UserData) {
        userPreferenceDataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userData.userId
            preferences[USER_EMAIL_KEY] = userData.email ?: ""
            preferences[DISPLAY_NAME_KEY] = userData.displayName ?: ""
            preferences[PROFILE_PICTURE_KEY] = userData.profilePicture ?: ""
        }
        Log.i(TAG, "User Data persisted in the DataStore; $userData")
    }

    private suspend fun getUserData(): UserData?{
        val preferences = userPreferenceDataStore.data.first()
        val userId = preferences[USER_ID_KEY]
        return if (userId != null){
            UserData(
                userId = userId,
                email = preferences[USER_EMAIL_KEY],
                displayName = preferences[DISPLAY_NAME_KEY],
                profilePicture = preferences[PROFILE_PICTURE_KEY],
            )
        }  else null
    }

    private suspend fun clearUserData(){
        userPreferenceDataStore.edit { it.clear() }
        Log.i(TAG, "Clear UserData from Data Store")
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val DISPLAY_NAME_KEY = stringPreferencesKey("display_name")
        private val PROFILE_PICTURE_KEY = stringPreferencesKey("profile_picture")
        private val SOURCE_KEY = stringPreferencesKey("source")
    }
}