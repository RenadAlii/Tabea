package com.renad.tabea.ui

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.timepicker.TimeFormat
import com.renad.tabea.data.DataSource
import com.renad.tabea.data.model.Todo
import java.text.SimpleDateFormat
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

    fun dateFormatter(): SimpleDateFormat {
        return SimpleDateFormat("dd/MM/yyyy")
    }

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

    // fun to return the system time format.
    fun setSystemHourFormat(context: Context): Int {
        // variable to make the time format match the device time format
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        // if the timeFormat in the system is 24 return it 24h else return  12h
        return if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
    }

    // fun the add and delete
    fun addTodoTask(todoText: String, details: String?) {
        setTodo(todoText)
        setDescription(details)
        addNewTask(
            Todo(
                _toDoTitle.value.toString(),
                _description.value.toString(),
                _time.value.toString(),
                _date.value.toString(),
                false,
            ),
        )
    }

    fun setIsCompleted(index: Int, IsCompleted: Boolean) {
        DataSource.setIsCompleted(IsCompleted, index)
        _isCompleted.value = IsCompleted
    }

    private fun addNewTask(task: Todo) {
        DataSource.addNewTask(task)
        setNotCompletedListSize()
    }

    fun deleteTask(task: Todo) {
        DataSource.deleteTask(task)
        setNotCompletedListSize()
    }

    fun editTask(index: Int, task: Todo) = DataSource.editTask(index, task)
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

    fun dateOfToday(): Date {
        val calendar = Calendar.getInstance()
        // set time to 00:00:00
        // so you will not face problem with the time when compare 2 date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}
