package com.boilerplate.util

import android.app.Activity
import android.app.Service
import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

fun Context.hideKeyboard(view: View) =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ).hideSoftInputFromWindow(view.windowToken, 0)

fun TextView.color(@ColorRes id: Int){
    this.setTextColor(ContextCompat.getColor(this.context, id))
}

fun EditText?.setFocus() {
    if (this == null)
        return

    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.requestFocus()
}

fun EditText?.hideSoftKeyboard(context: Context) {
    if (this == null)
        return
    val imm = context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun EditText?.showSoftKeyboard(context: Context) {
    if (this == null)
        return

    val imm = context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

fun EditText?.setNotFocus() {
    if (this == null)
        return

    this.isFocusable = false
    this.isFocusableInTouchMode = false
    this.requestFocus()
}

fun ProgressBar?.show(){
    if (this == null)
        return
    visibility = View.VISIBLE
}

fun ProgressBar?.hide(){
    if (this == null)
        return
    visibility = View.GONE
}

fun View?.show(){
    if (this == null)
        return
    visibility = View.VISIBLE
}

fun View?.hide(){
    if (this == null)
        return
    visibility = View.GONE
}

fun View?.invisible(){
    if (this == null)
        return
    visibility = View.INVISIBLE
}

fun View.snackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun isValidEmail(email: String?): Boolean {
    return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun String.NameTester(): Boolean {
    var valide = false
    for (i in 0 until this.length) {
        if (this[i] in 'A'..'Z' || this[i] in 'a'..'z' || this[i] == ' ') {
            valide = true
        } else {
            valide = false
            break
        }
    }
    return valide
}

fun String.currencyFormat(): String {
    val syms = DecimalFormatSymbols()
    syms.groupingSeparator = ' '
    val formatter = DecimalFormat("###,###,##0.000", syms)
    return formatter.format(this.toDouble())
}


fun roundOffDecimal(number: Double): Double? {
    val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}

fun <T> validateServiceInterface(service: Class<T>) {
    require(service.isInterface) { "API declarations must be interfaces." }
    // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
    // Android (http://b.android.com/58753) but it forces composition of API declarations which is
    // the recommended pattern.
    require(service.interfaces.size <= 0) { "API interfaces must not extend other interfaces." }
}