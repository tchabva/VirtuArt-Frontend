package uk.techreturners.virtuart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.techreturners.virtuart.data.repository.ArtworksRepository
import uk.techreturners.virtuart.data.repository.ArtworksRepositoryImpl
import uk.techreturners.virtuart.data.repository.ExhibitionsRepository
import uk.techreturners.virtuart.data.repository.ExhibitionsRepositoryImpl
import uk.techreturners.virtuart.data.repository.SearchRepository
import uk.techreturners.virtuart.data.repository.SearchRepositoryImpl
import uk.techreturners.virtuart.domain.repository.AuthRepository
import uk.techreturners.virtuart.domain.repository.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsExhibitionsRepository(
        exhibitionsRepositoryImpl: ExhibitionsRepositoryImpl
    ): ExhibitionsRepository

    @Binds
    @Singleton
    abstract fun bindsArtistsRepository(
        artworksRepositoryImpl: ArtworksRepositoryImpl
    ): ArtworksRepository

    @Binds
    @Singleton
    abstract fun bindsSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository
}