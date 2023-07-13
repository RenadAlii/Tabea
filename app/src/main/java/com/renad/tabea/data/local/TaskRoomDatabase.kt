package com.renad.tabea.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room Database that contains the Task table.
 */

@Database(entities = [LocalTask::class], version = 1)
abstract class TaskRoomDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}
