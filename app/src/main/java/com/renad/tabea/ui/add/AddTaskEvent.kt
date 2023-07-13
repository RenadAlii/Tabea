package com.renad.tabea.ui.add

sealed class AddTaskEvent {
    object ShowDatePicker : AddTaskEvent()
    data class HideDatePicker(val date: Long?, val isCancel: Boolean) : AddTaskEvent()
    object ShowTimePicker : AddTaskEvent()
    data class HideTimePicker(val time: String, val isCancel: Boolean) : AddTaskEvent()
    data class AddTask(val task: String, val taskDescription: String) : AddTaskEvent()
}
