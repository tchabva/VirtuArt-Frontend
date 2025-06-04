package uk.techreturners.virtuart.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


/*
Allows for creation of singletons for the APIs which can then be injected where they are required
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

}