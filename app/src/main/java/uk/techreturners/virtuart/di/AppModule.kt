package uk.techreturners.virtuart.di

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.remote.ArtworksApi
import uk.techreturners.virtuart.data.remote.ExhibitionsApi
import uk.techreturners.virtuart.domain.repository.AuthRepository
import uk.techreturners.virtuart.domain.repository.TokenManager
import javax.inject.Singleton

/*
Allows for creation of singletons for which can then be injected where they are required
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("user_preferences") }
        )
    }

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(
        authRepository: AuthRepository,
        tokenManager: TokenManager
    ): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val user = authRepository.getSignedInUser()

            val newRequestWithAuth = if (user != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer ${user.userId}")
                    .build()
            } else {
                originalRequest
            }

            val response = chain.proceed(newRequestWithAuth)

            // Handle 401 Response Unauthorised - token might be expired
            if (response.code == 401 && user != null){
                response.close()

                // Attempt to refresh token
                runBlocking {
                    val refreshSuccess = tokenManager.validateAndRefreshToken()
                    if (refreshSuccess) {
                        val refreshedUser = authRepository.getSignedInUser()
                        val retryRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer ${refreshedUser?.userId}")
                            .build()
                        chain.proceed(retryRequest)
                    } else {
                        // Token refresh failed, user needs to re-authenticate
                        authRepository.signOut()
                        response
                    }
                }
            } else {
                response
            }
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesExhibitionsApi(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ExhibitionsApi {
        return Retrofit
            .Builder()
            .baseUrl(context.getString(R.string.base_url_backend_wireless))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ExhibitionsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesArtworksApi(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ArtworksApi {
        return Retrofit
            .Builder()
            .baseUrl(context.getString(R.string.base_url_backend_wireless))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ArtworksApi::class.java)
    }
}