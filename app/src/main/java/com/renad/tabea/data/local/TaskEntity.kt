package com.renad.tabea.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

/**
 *  @see TaskEntity is a table for tasks.
 *  @param title represents the task title, such as "study for my math exam".
 *  @param details represents the task details, such as "from chapter 1 to 3" it's optional.
 *  @param time represents the time at which the task should be completed, it's optional.
 *  @param date represents the deadline for completing the task, it's optional.
 *  @param isCompleted represents the state of the task.
 */

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @Nonnull val title: String,
    val details: String?,
    val time: String?,
    val date: String?,
    @ColumnInfo(defaultValue = "FALSE")
    @Nonnull
    val isCompleted: Boolean,
)
