package com.renad.tabea.domain

import android.content.Context
import com.renad.tabea.R
import com.renad.tabea.core.util.Dispatcher
import com.renad.tabea.core.util.Response
import com.renad.tabea.data.TaskRepository
import com.renad.tabea.data.model.Task
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dispatcher: Dispatcher,
    @ActivityContext private val context: Context,
) {
    /**
     * Returns a list of tasks.
     *
     * @param taskId - the field used to sort the tasks. Default NONE = ASC sort.
     */
     operator fun invoke(taskId: Int): Flow<Response<Task>> {
        return taskRepository.getTaskById(taskId).transform { response ->
            when (response) {
                is Response.Success -> {
                    if (response.data == null) {
                        emit(Response.Failure(context.getString(R.string.task_not_found)))
                    }
                }
                else -> {}
            }
        }.flowOn(dispatcher.io)
    }
}
