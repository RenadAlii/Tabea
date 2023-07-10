package com.renad.tabea.ui.edit

sealed class EditTaskEvent {
    object ShowDatePicker : EditTaskEvent()
    data class HideDatePicker(val date: Long?, val isCancel: Boolean) : EditTaskEvent()
    object ShowTimePicker : EditTaskEvent()
    data class HideTimePicker(val time: String, val isCancel: Boolean) : EditTaskEvent()
    data class EditTask(val task: String, val taskDescription: String) : EditTaskEvent()
}
