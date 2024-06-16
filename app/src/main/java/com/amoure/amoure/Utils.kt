package com.amoure.amoure

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.TextView
import com.amoure.amoure.data.response.ProductItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

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

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPhoneNumberValid(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}


private val passwordREGEX = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-_.@]{8,}\$")
fun isPasswordValid(password: String): Boolean {
    return passwordREGEX.matcher(password).matches()
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getDummyProducts(): List<ProductItem> {
    return listOf(
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        ProductItem(
            ownerName = "Red Boutique",
            productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
            id = "1",
            imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"),
            rentPrice = 200000,
        ),
        )
}

fun getDummyProduct(): ProductItem {
    return ProductItem("Red Boutique",
        productName = "abfuehioajfp afhiafpajwfp apsdjpadp",
        id = "1",
        available = false,
        styleNotes = "abfuehioajfp afhiafpajwfp apsdjpadp abfuehioajfp afhiafpajwfp apsdjpadp abfuehioajfp afhiafpajwfp apsdjpadp",
        rating = 5.0,
        rentPrice = 200000,
        retailPrice = 10000000,
        description = "abfuehioajfp afhiafpajwfp apsdjpadp abfuehioajfp afhiafpajwfp apsdjpadp abfuehioajfp afhiafpajwfp apsdjpadp aiodwjoiajfoawjf haoigfj fpa",
        userId = "1",
        imgProduct = listOf("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png", "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png")
    )
}