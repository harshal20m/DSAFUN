package com.dsafun.app.di

import com.dsafun.app.domain.executor.TestCaseRunner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideTestCaseRunner(): TestCaseRunner {
        return TestCaseRunner()
    }
}

// Made with Bob
