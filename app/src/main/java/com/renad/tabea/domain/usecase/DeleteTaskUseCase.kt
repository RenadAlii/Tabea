package com.renad.tabea.domain.usecase

import com.renad.tabea.R
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
) {
    /**
     * Returns a list of tasks.
     *
     * @param taskId - the field used to sort the tasks. Default NONE = ASC sort.
     */
    operator fun invoke(taskId: Int): Flow<Response<Unit>> {
        return taskRepository.deleteById(taskId).transform { response ->
            when (response) {
                is Response.Failure -> {
                    emit(Response.Failure(R.string.could_not_delete_task))
                }

                else -> {}
            }
        }.flowOn(dispatcher.io)
    }
}
