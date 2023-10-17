package com.example.phonepeassignment.main.di

import android.content.Context
import com.example.phonepeassignment.main.data.DataRepository
import com.example.phonepeassignment.main.data.RawDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideDataRepository(
        @ApplicationContext context: Context
    ) : DataRepository {
        return RawDataRepository(context)
    }

}