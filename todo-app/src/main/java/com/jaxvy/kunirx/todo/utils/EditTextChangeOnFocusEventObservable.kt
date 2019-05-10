package com.jaxvy.kunirx.todo.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

fun EditText.textChangesWhenFocused() = EditTextAfterTextChangeWithFocusEventObservable(this)

// Does not trigger text changed events
fun EditText.setTextSilently(newText: String) {
    if (newText != text.toString()) {
        val isFocused = isFocused
        if (isFocused) {
            clearFocus()
        }
        setText(newText)
        setSelection(newText.length - 1)
        if (isFocused) {
            requestFocus()
        }
    }
}

class EditTextAfterTextChangeEvent(val text: String)

// Only triggered if users are typing (EditText is focused)
class EditTextAfterTextChangeWithFocusEventObservable(
    private val view: EditText
) : InitialValueObservable<EditTextAfterTextChangeEvent>() {

    override fun subscribeListener(observer: Observer<in EditTextAfterTextChangeEvent>) {
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.addTextChangedListener(listener)
    }

    override val initialValue get() = EditTextAfterTextChangeEvent(view.editableText.toString())

    private class Listener(
        private val view: EditText,
        private val observer: Observer<in EditTextAfterTextChangeEvent>
    ) : MainThreadDisposable(), TextWatcher {

        override fun beforeTextChanged(
            charSequence: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable) {
            if (view.isFocused) {
                observer.onNext(EditTextAfterTextChangeEvent(editable.toString()))
            }
        }

        override fun onDispose() {
            view.removeTextChangedListener(this)
        }
    }
}
