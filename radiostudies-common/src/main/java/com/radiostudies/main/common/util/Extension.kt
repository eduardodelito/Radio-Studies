package com.radiostudies.main.common.util

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Created by eduardo.delito on 8/17/20.
 */
/**
 * Hide Keyboard in the Fragment.
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/**
 * Hide Keyboard in the Activity.
 */
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * Hide keyboard method.
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Method to simplified how to set an Observer by just passing the [body] to be executed inside the observer.
 */
inline fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, crossinline body: (T?) -> Unit) =
    liveData.observe(this, Observer<T?> { t -> body(t) })

fun <T : Any, L : LiveData<T>> LifecycleOwner.unObserve(liveData: L) = liveData.removeObservers(this)
/**
 * We need to restart the observer because it stays observing until the LifecycleOwner is destroyed.
 * In the case of Fragments, they are not destroyed when the fragment is detached/reattached, and a new
 * observer could be added every time the Fragment is shown and onActivityCreated() is executed.
 * By doing this, we make sure we only have ONE observer at a time.
 * Refer to: https://medium.com/@BladeCoder/architecture-components-pitfalls-part-1-9300dd969808
 */
inline fun <T : Any, L : LiveData<T>> LifecycleOwner.reObserve(liveData: L, crossinline body: (T?) -> Unit) {
    unObserve(liveData)
    observe(liveData, body)
}

/**
 * Enable/Disable AppCompatTextView.
 * @param message set message value if available.
 */
fun AppCompatTextView.setViewVisibility(message: String?) {
    with(this) {
        visibility = message?.let {
            text = message
            View.VISIBLE
        } ?: View.GONE
    }
}

fun Button.setEnable(enable: Boolean) {
    with(this) {
        if (enable) {
            isEnabled = true
            alpha = 1f
        } else {
            isEnabled = false
            alpha = 0.5f
        }
    }
}

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}