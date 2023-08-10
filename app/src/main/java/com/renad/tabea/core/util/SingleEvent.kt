package com.renad.tabea.core.util

data class SingleEvent<out T>(
    val value: T,
) {

    var isHandled: Boolean = false
        private set

    private fun getContentIfNotHandled(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            value
        }
    }

    fun handel(collector: (T) -> Unit) {
        getContentIfNotHandled()?.let(collector)
    }
}
