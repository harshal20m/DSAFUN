package com.dsafun.app.di

import com.dsafun.app.data.repository.ProblemRepositoryImpl
import com.dsafun.app.domain.repository.ProblemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProblemRepository(
        impl: ProblemRepositoryImpl
    ): ProblemRepository
}

// Made with Bob
