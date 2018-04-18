package ru.vassuv.appgallery.utils.atlibrary.uicomponent

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

inline fun EditText.addTextChangedListener(crossinline block: (text: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            block(p0.toString())
        }
    })
}
