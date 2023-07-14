package com.renad.tabea.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.renad.tabea.core.util.DateUtil.dateFormatter
import com.renad.tabea.data.DataSource
import com.renad.tabea.domain.model.Task
import com.renad.tabea.domain.model.TaskState
import java.util.*

class TodoViewModel : ViewModel() {

    private var _notCompletedListSize = MutableLiveData(DataSource.getTasksSize().toString())
    val notCompletedListSize get() = _notCompletedListSize

    private var _toDoTitle = MutableLiveData<String>()
    val toDo get() = _toDoTitle

    private var _isChecked = MutableLiveData(false)
    val isChecked get() = _isChecked

    private var _description = MutableLiveData<String>()
    val description get() = _description

    private var _time = MutableLiveData("")

    val time get() = _time

    private var _date = MutableLiveData<String>()
    val date get() = _date

    private var _isCompleted = MutableLiveData<Boolean>()
    val isCompleted get() = _isCompleted

// fun start

    // fun's set
    fun setDate(selectedDate: Long?) {
        // to format the date
        _date.value = dateFormatter().format(selectedDate)
    }

    fun setTime(time: String) {
        _time.value = time
    }

    private fun setTodo(newTodo: String) {
        _toDoTitle.value = newTodo
    }

    private fun setDescription(newDescription: String?) {
        _description.value = newDescription ?: ""
    }

    // funs to check if the data is not null
    fun isDataNotEmpty(todoText: String, time: String, date: String): Boolean {
        val todoTaskCheck = isTodoTaskNotEmpty(todoText)
        val timeCheck = isTimeNotEmpty(time)
        val dateCheck = isDateNotEmpty(date)
        return todoTaskCheck && timeCheck && dateCheck
    }

    private fun isDateNotEmpty(date: String): Boolean = date != ""

    private fun isTimeNotEmpty(time: String): Boolean = time != ""

    private fun isTodoTaskNotEmpty(todoText: String): Boolean = todoText != ""

    // fun the add and delete
    fun addTodoTask(todoText: String, details: String?) {
        setTodo(todoText)
        setDescription(details)
        addNewTask(
            Task(
                _toDoTitle.value.toString(),
                _description.value.toString(),
                _time.value.toString(),
                _date.value.toString(),
                false,
                TaskState.ACTIVE,
            ),
        )
    }

    fun setIsCompleted(task: Task) {
        DataSource.setIsCompleted(!task.isCompleted, task)
        _isCompleted.value = !task.isCompleted
    }

    private fun addNewTask(task: Task) {
        DataSource.addNewTask(task)
        setNotCompletedListSize()
    }

    fun deleteTask(task: Task) {
        DataSource.deleteTask(task)
        setNotCompletedListSize()
    }

    fun editTask(index: Int, task: Task) = DataSource.editTask(index, task)
    fun setNotCompletedListSize() {
        _notCompletedListSize.value = DataSource.getTasksSize().toString()
        println(notCompletedListSize.value)
    }

    fun sortByASC() {
        DataSource.sortByAES()
        println(DataSource.taskList)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sortByDES() {
        DataSource.sortByDES()
        println(DataSource.taskList)
    }

    fun sortByDate() {
        DataSource.sortByDate()
        println(DataSource.taskList)
    }

    fun clearList() {
        DataSource.clearList()
    }

}
