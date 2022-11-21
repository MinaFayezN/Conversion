package dev.mina.conversion.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mina.conversion.data.DetailsRepo
import dev.mina.conversion.data.DetailsRepoImpl
import dev.mina.conversion.data.ExchangeRepo
import dev.mina.conversion.data.ExchangeRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun exchangeRepo(repo: ExchangeRepoImpl): ExchangeRepo

    @Binds
    @Singleton
    abstract fun detailsRepo(repo: DetailsRepoImpl): DetailsRepo

}

