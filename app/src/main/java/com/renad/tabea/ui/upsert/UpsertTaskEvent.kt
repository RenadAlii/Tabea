package com.renad.tabea.ui.upsert

sealed class UpsertTaskEvent {
    object ShowDatePicker : UpsertTaskEvent()
    data class HideDatePicker(val date: Long?, val isCancel: Boolean) : UpsertTaskEvent()
    object ShowTimePicker : UpsertTaskEvent()
    data class HideTimePicker(val time: String, val isCancel: Boolean) : UpsertTaskEvent()
    data class EditTask(val task: String, val taskDescription: String) : UpsertTaskEvent()
}
