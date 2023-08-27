package com.renad.tabea.core.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.setEditTextValue(text: String) {
    editText?.setText(text)
    editText?.setSelection(text.length)
}
