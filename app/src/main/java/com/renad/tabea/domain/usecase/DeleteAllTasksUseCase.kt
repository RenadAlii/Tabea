package com.renad.tabea.domain.usecase

import com.renad.tabea.R
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

/**
 * A use case which obtains a list of tasks.
 */
class DeleteAllTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
) {
    /**
     * Returns a list of tasks.
     */
    operator fun invoke(): Flow<Response<Unit>> {
        return taskRepository.deleteAll().transform { response ->
            when (response) {
                is Response.Failure -> emit(Response.Failure(R.string.could_not_delete_tasks))
                else -> { emit(response) }
            }
        }.flowOn(dispatcher.io)
    }
}
