package com.amoure.amoure

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import java.text.NumberFormat
import java.util.Locale

fun Int.withCurrencyFormat(): String {
    val mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return mCurrencyFormat.format(this)
}

fun TextView.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        var timer: CountDownTimer? = null

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            timer?.cancel()
            timer = object : CountDownTimer(2000, 3000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    afterTextChanged.invoke(editable.toString())
                }
            }.start()
        }
    })
}

fun String.currencyStringToInteger(): Int {
    val regex = Regex("[^0-9]")
    val cleanString = regex.replace(this, "")
    return cleanString.toInt()
}