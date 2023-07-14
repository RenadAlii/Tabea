//package com.renad.tabea.ui.add
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.renad.tabea.core.util.Dispatcher
//import com.renad.tabea.core.util.Response
//import com.renad.tabea.domain.TaskRepository
//import com.renad.tabea.data.model.Task
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.flowOn
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import javax.inject.Inject
//
//@HiltViewModel
//class AddTaskViewModel @Inject constructor(
//    private val taskRepository: TaskRepository,
//    private val dispatcher: Dispatcher,
//) : ViewModel() {
//
//    fun addTask(task: Task) {
//        taskRepository.insert(task).onEach {
//            handelResponse(it)
//        }.flowOn(dispatcher.io).launchIn(viewModelScope)
//    }
//
//    private fun handelResponse(response: Response<Unit>) {
//        when (response) {
//            is Response.Failure -> {
//                // show error msg
//            }
//
//            Response.Loading -> {
//                // show loading
//            }
//
//            is Response.Success -> {
//                // navigate back and show success msg that said the task is added
//            }
//        }
//    }
//
//    // fun to check if the data is not null
//    fun isDataNotEmpty(todoText: String, time: String, date: String): Boolean {
//        val todoTaskCheck = isTodoTaskNotEmpty(todoText)
//        val timeCheck = isTimeNotEmpty(time)
//        val dateCheck = isDateNotEmpty(date)
//        return todoTaskCheck && timeCheck && dateCheck
//    }
//
//    private fun isDateNotEmpty(date: String): Boolean = date != ""
//
//    private fun isTimeNotEmpty(time: String): Boolean = time != ""
//
//    private fun isTodoTaskNotEmpty(todoText: String): Boolean = todoText != ""
//}
