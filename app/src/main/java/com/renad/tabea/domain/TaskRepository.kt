package com.renad.tabea.domain

import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.model.SortType
import com.renad.tabea.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<Response<List<Task>>>

    fun getAllTasksSorted(sortType: SortType): Flow<Response<List<Task>>>

    fun getTaskById(taskId: Int): Flow<Response<Task?>>

    fun upsert(task: Task): Flow<Response<Unit>>

    fun update(task: Task): Flow<Response<Unit>>

    fun updateCompleted(taskId: Int, completed: Boolean): Flow<Response<Unit>>

    fun delete(task: Task): Flow<Response<Unit>>

    fun deleteById(taskId: Int): Flow<Response<Unit>>

    fun deleteAll(): Flow<Response<Unit>>
}
