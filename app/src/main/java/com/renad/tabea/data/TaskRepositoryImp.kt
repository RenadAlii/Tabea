package com.renad.tabea.data

import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.data.local.LocalTask
import com.renad.tabea.data.local.TaskDao
import com.renad.tabea.data.model.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImp @Inject constructor(
    private val localDataSource: TaskDao,
    private val dispatcher: Dispatcher,
) : TaskRepository {

    override fun getAllTasks(): Flow<Response<List<Task>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTasksSortByASC(): Flow<Response<List<Task>>> = flowCall {
        localDataSource.getAllTasksSortByASC()
    }.flowOn(dispatcher.io)

    override suspend fun getAllSortByDESC(): Flow<Response<List<Task>>> = flowCall {
        localDataSource.getAllSortByDESC()
    }.flowOn(dispatcher.io)

    override suspend fun getTaskById(taskId: Int): Flow<Response<LocalTask>> = flowCall {
        localDataSource.getTaskById(taskId)
    }

    override suspend fun insert(vararg task: LocalTask) = flowCall {
        localDataSource.insert(task)
    }.flowOn(dispatcher.io)

    override suspend fun update(task: LocalTask) = flowCall {
        localDataSource.update(task)
    }.flowOn(dispatcher.io)

    override suspend fun updateCompleted(taskId: String, completed: Boolean) = flowCall {
        localDataSource.updateCompleted(taskId, completed)
    }.flowOn(dispatcher.io)

    override suspend fun delete(task: LocalTask) = flowCall {
        localDataSource.delete(task)
    }.flowOn(dispatcher.io)

    override suspend fun deleteById(taskId: String) = flowCall {
        localDataSource.deleteById(taskId)
    }.flowOn(dispatcher.io)

    override suspend fun deleteAll() = flowCall {
        localDataSource.deleteAll()
    }.flowOn(dispatcher.io)
}

fun <T> TaskRepository.flowCall(block: suspend () -> T) = callbackFlow {
    try {
        this.send(Response.Success(block()))
    } catch (e: Throwable) {
        this.trySend(Response.Failure(e.message ?: "An unknown error occurred."))
    }
    awaitClose { println("flowCall closed!") }
}
