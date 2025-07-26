package uk.techreturners.virtuart.di

import android.content.Context
import android.util.Log
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
import uk.techreturners.virtuart.data.remote.SearchApi
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
            val currentUser = authRepository.getSignedInUser()

            val newRequestWithAuth = if (currentUser != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer ${currentUser.userId}")
                    .build()
            } else {
                originalRequest
            }

            // Execute the request
            val response = chain.proceed(newRequestWithAuth)

            if (response.code == 401 && currentUser != null) {
                Log.w("AuthInterceptor", "Received 401, attempting token refresh")

                response.close() // closes the original response

                // Attempt a token refresh
                val refreshSuccess = runBlocking {
                    tokenManager.tokenRefresh()
                }

                if (refreshSuccess) {
                    Log.i("AuthInterceptor", "Token refresh successful, retrying request")

                    // Get the updated user with the new token
                    val updatedUser = authRepository.getSignedInUser()
                    val retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer ${updatedUser?.userId}")
                        .build()

                    chain.proceed(retryRequest)
                } else {
                    Log.e("AuthInterceptor", "Token Refresh Failed")
                    chain.proceed(originalRequest)
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

    @Provides
    @Singleton
    fun providesSearchApi(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): SearchApi {
        return Retrofit
            .Builder()
            .baseUrl(context.getString(R.string.base_url_backend_wireless))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(SearchApi::class.java)
    }
}