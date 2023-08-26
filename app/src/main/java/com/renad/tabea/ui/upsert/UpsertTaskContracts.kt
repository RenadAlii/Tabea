package com.renad.tabea.ui.upsert

import com.renad.tabea.core.util.SingleEvent

sealed class UpsertTaskEvent {
    object ShowDatePicker : UpsertTaskEvent()
    data class HideDatePicker(val date: Long?, val isCancel: Boolean) : UpsertTaskEvent()
    data class EditTask(val task: String) : UpsertTaskEvent()
    data class EditDescription(val taskDescription: String) : UpsertTaskEvent()
    object SaveText : UpsertTaskEvent()
}
data class UpsertTaskUiState(
    val task: String = "",
    val description: String? = "",
    val date: String? = null,
    val showDatePicker: SingleEvent<Unit>? = null,
    val clickEdit: Boolean = false,
    val errorMsg: SingleEvent<Int>? = null,
    val isLoading: Boolean = true,
    val isCompleted: Boolean = false,
    val navigateBack: SingleEvent<Unit>? = null,
)
