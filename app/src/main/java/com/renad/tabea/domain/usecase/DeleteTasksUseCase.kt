package com.renad.tabea.domain.usecase

import com.renad.tabea.R
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.data.toLocal
import com.renad.tabea.domain.TaskRepository
import com.renad.tabea.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DeleteTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
) {

    operator fun invoke(tasks: List<Task>): Flow<Response<Unit>> {
        return taskRepository.delete(*tasks.toLocal().toTypedArray()).map { response ->
            when (response) {
                is Response.Failure -> Response.Failure(R.string.could_not_delete_tasks)
                else -> response
            }
        }.flowOn(dispatcher.io)
    }
}
