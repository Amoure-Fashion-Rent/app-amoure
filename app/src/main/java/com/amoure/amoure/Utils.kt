package com.amoure.amoure

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.util.Calendar
import android.net.Uri
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.TextView
import androidx.exifinterface.media.ExifInterface
import com.amoure.amoure.data.response.Owner
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.RentItem
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.data.response.ReviewUser
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}

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

fun String.formatDate(): String {
    try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date? = isoFormat.parse(this)

        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

        return date?.let { outputFormat.format(it) } ?: "Invalid date"
    } catch (e: ParseException) {
        e.printStackTrace()
        return "Invalid date"
    }
}

fun String.removeUnderscoreAndCapitalize(): String {
    return this.replace(Regex("_(.)")) { matchResult ->
        matchResult.groupValues[1].uppercase()
    }
}

fun Calendar.formatCalendarToISO8601(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(this.time)
}

fun String.formatCalendarToISO8601(): String {
    val initialFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    val date: Date? = initialFormat.parse(this)

    date?.let {
        val newFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        newFormat.timeZone = TimeZone.getTimeZone("UTC")

        return newFormat.format(it)
    } ?: run {
        return "Failed to parse the date"
    }
}

fun categoryImages(): List<String> {
    return listOf(
        "https://storage.googleapis.com/img-product-amoure/13_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/113_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/222_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/308_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/377_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/661_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/721_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/781_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/841_0.jpg",
        "https://storage.googleapis.com/img-product-amoure/901_0.jpg"
    )
}


fun getProducts(): List<ProductItem> {
    return listOf(
        ProductItem(
            id = 1,
            name = "CAMELLIA DRESS",
            images = listOf("https://pc-ap.rtrcdn.com/productimages/front/1080x/12/SAB37.jpg?auto=webp&optimize=medium&width=1080"),
            retailPrice = 4699900,
            rentPrice = 335000,
            color = "Red",
            status = "AVAILABLE",
            owner = Owner(5, "Sabina Musayev"),
    ),
        ProductItem(
            id = 2,
            name = "JACQUARD BLAZER WITH BOW",
            images = listOf("https://static.zara.net/assets/public/14df/ca40/3ef643fa8ecc/d5ab60ce6f32/03252565500-p/03252565500-p.jpg?ts=1715860590498&w=48"),
            retailPrice = 1699900,
            rentPrice = 285000,
            color = "Green",
            status = "AVAILABLE",
            owner = Owner(3, "JACQUARD"),
        ),
        ProductItem(
            id = 3,
            name = "BLAZER WITH POCKETS",
            images = listOf("https://static.zara.net/assets/public/a376/31f6/af2f430ea568/44576a517ccc/02776826704-p/02776826704-p.jpg?ts=1711959120153&w=48"),
            retailPrice = 1699900,
            rentPrice = 285000,
            color = "Brown",
            status = "AVAILABLE",
            owner = Owner(1, "ZARA"),
        ),
        ProductItem(
            id = 4,
            name = "FLOWING FADED TRENCH COAT",
            images = listOf("https://static.zara.net/assets/public/8d8f/2309/59204eb68cac/c4b4522057f6/04877042743-p/04877042743-p.jpg?ts=1712164068480&w=48"),
            retailPrice = 1699900,
            rentPrice = 285000,
            color = "Green",
            status = "AVAILABLE",
            owner = Owner(2, "Chamberlain Blazer"),
        ),
        ProductItem(
            id = 5,
            name = "ROLL-UP SLEEVE LINEN-BLEND SHIRT",
            images = listOf("https://static.zara.net/assets/public/a1e0/1114/0aca4e2e9367/fb9562c4284e/02157041250-p/02157041250-p.jpg?ts=1713528095320&w=48"),
            retailPrice = 1629900,
            rentPrice = 231500,
            color = "White",
            status = "ON_RENT",
            owner = Owner(2, "ZARA"),
        ),
        ProductItem(
            id = 6,
            name = "DENIM MINI SKIRT",
            images = listOf("https://static.zara.net/assets/public/a41f/b5e6/ded34df78981/134ee8a17c9c/06164090407-p/06164090407-p.jpg?ts=1711355457314&w=48"),
            retailPrice = 1699900,
            rentPrice = 235000,
            color = "Blue",
            status = "AVAILABLE",
            owner = Owner(4, "Zadig & Voltaire"),
        ),
        ProductItem(
            id = 7,
            name = "EMBROIDERED BLOUSE WITH WOODEN BUTTONS",
            images = listOf("https://static.zara.net/assets/public/b9be/ac45/e083412ba325/3596d6e0cc55/08351032069-p/08351032069-p.jpg?ts=1713953863337&w=48"),
            retailPrice = 1799900,
            rentPrice = 240000,
            color = "Off-White",
            status = "AVAILABLE",
            owner = Owner(4, "Zadig & Voltaire"),
        ),
        ProductItem(
            id = 8,
            name = "CARROT-FIT TROUSERS WITH CUFFED HEMS",
            images = listOf("https://static.zara.net/assets/public/978c/808f/2ad84ceb9203/655c08e15fe9/01608022800-15-p/01608022800-15-p.jpg?ts=1707931437165&w=48"),
            retailPrice = 1799900,
            rentPrice = 240000,
            color = "Other",
            status = "AVAILABLE",
            owner = Owner(3, "JACQUARD"),
        ),
    )
}

fun getProduct(): ProductItem {
    return ProductItem(
        id = 1,
        name = "CAMELLIA DRESS",
        images = listOf("https://pc-ap.rtrcdn.com/productimages/front/1080x/12/SAB37.jpg?auto=webp&optimize=medium&width=1080",
            "https://pc-ap.rtrcdn.com/productimages/back/1080x/12/SAB37.jpg?auto=webp&optimize=medium&width=183",
            "https://pc-ap.rtrcdn.com/productimages/side/1080x/12/SAB37.jpg?auto=webp&optimize=medium&width=183",
            "https://pc-ap.rtrcdn.com/productimages/editorial/1080x/12/SAB37.jpg?auto=webp&optimize=medium&width=183"),
        description = "Red gown (Ground 100% Polyester, Embroidery 100% Cotton). V-neck. Back zipper closure. See size & fit notes for length and measurements. Imported.",
        stylishNotes = "This vibrant red tiered maxi dress by Sabina Musayev gives off a romantic and bohemian flair. Accentuate its deep V-neckline with delicate jewelry. Pair with strappy sandals for a chic summer evening look.",
        retailPrice = 4699900,
        rentPrice = 335000,
        color = "Red",
        status = "AVAILABLE",
        owner = Owner(5, "Sabina Musayev"),
        avgRating = 4.7,
        reviewsCount = 3
    )
}

fun getReviews(): List<ReviewItem> {
    return listOf(
        ReviewItem(
            id = 1,
            rating = 5,
            comment = "Penampilan yang luar biasa!! Sempurna dalam segala hal terutama jika berdada kecil. Perhatikan bahwa bagian belakang sedikit lebih panjang dari depan jadi hati-hati saat menari.",
            productId = 1,
            user = ReviewUser(1, "Dewi")
        ),
        ReviewItem(
            id = 2,
            rating = 5,
            comment = "Suka sekali, mendapat banyak pujian. Agak panjang pada saya meskipun sudah memakai sepatu hak tinggi, saya tinggi 168 cm.",
            productId = 1,
            user = ReviewUser(2, "Fitri")
        ),
        ReviewItem(
            id = 3,
            rating = 4,
            comment = "Saya biasanya memakai ukuran medium untuk gaun, dan memakai ukuran medium tetapi mungkin akan memakai ukuran besar jika tersedia. Saya pendek jadi lebih suka memakai medium, tetapi sebenarnya sangat ketat di bagian tulang rusuk. Meski begitu, tidak aneh bagi bagian atas pakaian ketat di tulang rusuk saya. Gaunnya juga sangat panjang dan saya harus memakai sepatu hak 13 cm. Tapi saya merasa seperti seorang bintang!!!",
            productId = 1,
            user = ReviewUser(3, "Ayu")
        ),
    )
}

fun getRentHistory(): List<RentItem> {
    return listOf(
        RentItem(
            productId = 1,
            productName = "CAMELLIA DRESS",
            product = ProductItem(
                images = listOf("https://pc-ap.rtrcdn.com/productimages/front/1080x/12/SAB37.jpg?auto=webp&optimize=medium&width=1080"),
                owner = Owner(1, "Sabina Musayev")
            ),
            status = "CONFIRMED",
            rentalStartDate = "2024-06-20T11:45:43.560Z",
            rentalEndDate = "2024-06-24T11:45:43.560Z",
        ),
        RentItem(
            productId = 2,
            productName = "DARTED WIDE-LEG TROUSERS",
            product = ProductItem(
                images = listOf("https://static.zara.net/assets/public/8436/6e6e/6fd341edb139/999169489999/03220564630-p/03220564630-p.jpg?ts=1713355163664&w=48"),
                owner = Owner(1, "Zadig & Voltaire")
            ),
            status = "IN_PROGRESS",
            rentalStartDate = "2024-06-16T11:45:43.560Z",
            rentalEndDate = "2024-06-20T11:45:43.560Z",
        ),
        RentItem(
            productId = 3,
            productName = "OVERSIZED FAUX LEATHER BIKER JACKET",
            product = ProductItem(
                images = listOf("https://static.zara.net/assets/public/3c8d/6d3b/9ed445af9a49/4f2d33e76d4b/03414002250-p/03414002250-p.jpg?ts=1715844053847&w=48"),
                owner = Owner(1, "Zadig & Voltaire")
            ),
            status = "COMPLETED",
            rentalStartDate = "2024-06-10T11:45:43.560Z",
            rentalEndDate = "2024-06-14T11:45:43.560Z",
        ),
    )
}