package com.renad.tabea.data

import com.renad.tabea.core.util.Response
import com.renad.tabea.data.local.LocalTask
import com.renad.tabea.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<Response<List<Task>>>

    suspend fun getAllTasksSortByASC(): Flow<Response<List<Task>>>

    suspend fun getAllSortByDESC(): Flow<Response<List<Task>>>

    suspend fun getTaskById(taskId: Int): Flow<Response<LocalTask>>

    suspend fun insert(vararg task: LocalTask): Flow<Response<Unit>>

    suspend fun update(task: LocalTask): Flow<Response<Unit>>

    suspend fun updateCompleted(taskId: String, completed: Boolean): Flow<Response<Unit>>

    suspend fun delete(task: LocalTask): Flow<Response<Unit>>

    suspend fun deleteById(taskId: String): Flow<Response<Unit>>

    suspend fun deleteAll(): Flow<Response<Unit>>
}
