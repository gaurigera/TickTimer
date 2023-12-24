package com.example.servicenotificationapp.timerServices.shared

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimerRepositoryModule {
    @Provides
    @Singleton
    fun provideTimerRepository(): TimerRepository {
        return TimerRepository
    }
}