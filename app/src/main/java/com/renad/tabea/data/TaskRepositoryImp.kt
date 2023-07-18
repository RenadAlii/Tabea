package com.renad.tabea.data

import com.renad.tabea.R
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.data.local.TaskDao
import com.renad.tabea.domain.model.Task
import com.renad.tabea.domain.TaskRepository
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

    override fun getAllTasksSortByASC(): Flow<Response<List<Task>>> = flowCall {
        localDataSource.getAllTasksSortByASC().toExternal()
    }.flowOn(dispatcher.io)

    override fun getAllSortByDESC(): Flow<Response<List<Task>>> = flowCall {
        localDataSource.getAllSortByDESC().toExternal()
    }.flowOn(dispatcher.io)

    override fun getTaskById(taskId: Int): Flow<Response<Task?>> = flowCall {
        localDataSource.getTaskById(taskId)?.toExternal()
    }

    override fun upsert(task: Task) = flowCall {
        localDataSource.upsert(task.toLocal())
    }.flowOn(dispatcher.io)

    override fun update(task: Task) = flowCall {
        localDataSource.update(task.toLocal())
    }.flowOn(dispatcher.io)

    override fun updateCompleted(taskId: String, completed: Boolean) = flowCall {
        localDataSource.updateCompleted(taskId, completed)
    }.flowOn(dispatcher.io)

    override fun delete(task: Task) = flowCall {
        localDataSource.delete(task.toLocal())
    }.flowOn(dispatcher.io)

    override fun deleteById(taskId: Int) = flowCall {
        localDataSource.deleteById(taskId)
    }.flowOn(dispatcher.io)

    override fun deleteAll() = flowCall {
        localDataSource.deleteAll()
    }.flowOn(dispatcher.io)
}

fun <T> TaskRepository.flowCall(block: suspend () -> T) = callbackFlow {
    try {
        this.send(Response.Success(block()))
    } catch (e: Throwable) {
        this.trySend(Response.Failure(R.string.unknown_error_occurred))
    }
    awaitClose { println("flowCall closed!") }
}
