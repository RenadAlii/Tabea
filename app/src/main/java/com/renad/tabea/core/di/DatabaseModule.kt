package com.renad.tabea.core.di

import android.content.Context
import androidx.room.Room
import com.renad.tabea.data.local.TaskDao
import com.renad.tabea.data.local.TaskRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): TaskRoomDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskRoomDatabase::class.java,
        "task.db",
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideTaskDao(database: TaskRoomDatabase): TaskDao = database.taskDao()
}
