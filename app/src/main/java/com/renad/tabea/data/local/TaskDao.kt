package com.renad.tabea.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * @see TaskDao Data Access Object for the task table.
 */

@Dao
interface TaskDao {

    /**
     * Observable query that emit new Tasks whenever there are changes
     * Sort By ASC
     */
    @Query("SELECT * FROM task ORDER BY task ASC")
    fun getAllTasks(): Flow<List<LocalTask>>

    /**
     * One-shot query that only runs once and return snapshot of Tasks at time of execution
     * Sort By ASC
     */
    @Query("SELECT * FROM task ORDER BY task ASC")
    suspend fun getAllTasksSortByASC(): List<LocalTask>

    /**
     * One-shot query that only runs once and return snapshot of Tasks
     * Sort By DESC
     */
    @Query("SELECT * FROM task ORDER BY task DESC")
    suspend fun getAllSortByDESC(): List<LocalTask>

    /**
     * get task by date
     * @param taskDate date of the task
     */
    @Query("SELECT * from task WHERE date = :taskDate")
    suspend fun getTasksByDate(taskDate: String): List<LocalTask>

    /**
     * get task by time
     * @param taskTime time of the task
     */
    @Query("SELECT * from task WHERE time = :taskTime")
    suspend fun getTasksByTime(taskTime: String): List<LocalTask>

    /**
     * get task by id
     * @param taskId id of the task
     */
    @Query("SELECT * FROM task WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): LocalTask?

    /**
     * Insert or update task.
     */
    @Upsert
    suspend fun upsert(task: LocalTask)

    /**
     * Update task.
     */
    @Update
    suspend fun update(task: LocalTask)

    /**
     * Update the complete status of  task
     * @param taskId id of the task
     * @param completed status to be updated
     */
    @Query("UPDATE task SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: String, completed: Boolean)

    /**
     * Delete task.
     */
    @Delete
    suspend fun delete(task: LocalTask)

    /**
     * Delete a task by id.
     */
    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteById(taskId: Int)

    /**
     * Delete all tasks.
     */
    @Query("DELETE FROM task")
    suspend fun deleteAll()
//    @Delete
//    suspend fun deleteAll()
}
