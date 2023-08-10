package com.renad.tabea.ui.upsert

import com.renad.tabea.core.util.SingleEvent

sealed class UpsertTaskEvent {
    object ShowDatePicker : UpsertTaskEvent()
    data class HideDatePicker(val date: Long?, val isCancel: Boolean) : UpsertTaskEvent()
    object ShowTimePicker : UpsertTaskEvent()
    data class HideTimePicker(val time: String, val isCancel: Boolean) : UpsertTaskEvent()
    data class EditTask(val task: String, val taskDescription: String) : UpsertTaskEvent()
}
data class UpsertTaskUiState(
    val task: String = "",
    val description: String? = "",
    val date: String? = null,
    val time: String? = "",
    val showDatePicker: SingleEvent<Unit>? = null,
    val showTimePicker: SingleEvent<Unit>? = null,
    val clickEdit: Boolean = false,
    val errorMsg: SingleEvent<Int>? = null,
    val isLoading: Boolean = true,
    val isCompleted: Boolean = false,
    val navigateBack: SingleEvent<Unit>? = null,
)
