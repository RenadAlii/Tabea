package com.renad.tabea.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

/**
 *  @see LocalTask is a table for tasks.
 *  @param task represents the task, such as "study for my math exam".
 *  @param details represents the task details, such as "from chapter 1 to 3" it's optional.
 *  @param date represents the deadline for completing the task, it's optional.
 *  @param isCompleted represents the state of the task.
 */

@Entity(tableName = "task")
data class LocalTask(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @Nonnull val task: String,
    val details: String?,
    val date: Long?,
    @ColumnInfo(defaultValue = "FALSE")
    @Nonnull
    val isCompleted: Boolean,
)
