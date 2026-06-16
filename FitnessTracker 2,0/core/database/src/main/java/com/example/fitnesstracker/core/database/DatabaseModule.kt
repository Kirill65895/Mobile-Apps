package com.example.fitnesstracker.core.database

import android.content.Context
import androidx.room.Room
import com.example.fitnesstracker.core.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt-модуль общего источника данных. Предоставляет DAO другим модулям. */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FitnessDatabase =
        Room.databaseBuilder(context, FitnessDatabase::class.java, Constants.DATABASE_NAME).build()

    @Provides
    fun provideWorkoutDao(db: FitnessDatabase): WorkoutDao = db.workoutDao()
}
