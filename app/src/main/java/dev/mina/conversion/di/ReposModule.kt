package dev.mina.conversion.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mina.conversion.data.ExchangeRepo
import dev.mina.conversion.data.ExchangeRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun converterRepo(repo: ExchangeRepoImpl): ExchangeRepo

}

