package uk.techreturners.virtuart.di

import android.content.Context
import androidx.credentials.CredentialManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.techreturners.virtuart.R
import uk.techreturners.virtuart.data.remote.ExhibitionsApi
import uk.techreturners.virtuart.domain.repository.AuthRepository
import javax.inject.Singleton


/*
Allows for creation of singletons for which can then be injected where they are required
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(authRepository: AuthRepository): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val user = authRepository.getSignedInUser()

            val newRequest = if (user != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer ${user.userId}")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
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
}