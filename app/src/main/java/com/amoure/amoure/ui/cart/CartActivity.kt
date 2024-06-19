package com.amoure.amoure.ui.cart

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.request.PostCartRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.Owner
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ActivityCartBinding
import com.amoure.amoure.formatCalendarToISO8601
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.withCurrencyFormat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import kotlin.properties.Delegates


class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var req: PostCartRequest
    private lateinit var product: ProductItem
    private var productId by Delegates.notNull<Int>()
    private lateinit var productName: String
    private lateinit var ownerName: String
    private lateinit var imageUrl: String
    private var rentPrice by Delegates.notNull<Int>()
    private lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvCart.layoutManager = LinearLayoutManager(this)

        productId = intent.getStringExtra(PRODUCT_ID).toString().toInt()
        productName = intent.getStringExtra(PRODUCT_NAME).toString()
        ownerName = intent.getStringExtra(OWNER_NAME).toString()
        rentPrice = intent.getStringExtra(RENT_PRICE).toString().toInt()
        imageUrl = intent.getStringExtra(IMAGE_URL).toString()
        val size = intent.getStringExtra(SIZE).toString()
        val color = intent.getStringExtra(COLOR).toString()
        product = ProductItem(owner = Owner(fullName = ownerName), id = productId, rentPrice = rentPrice, name = productName, images = listOf(imageUrl))
        req = PostCartRequest(productId, "",  "", )

        cartViewModel.response.observe(this) {
            showAlert(it)
        }

        cartViewModel.profile.observe(this) {
            binding.tvAddress.text = it.addressDetail
            address = it.addressDetail.toString()
        }

        cartViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        cartViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.topAppBarSecond.setNavigationOnClickListener {
            finish()
        }
        putCartDetail()
        setCarts(listOf(product))
        postCart()
    }

    private fun postCart() {
        with(binding) {
            btCheckout.setOnClickListener {
                val rentalPeriod = edRentalPeriod.text.toString()
                if (rentalPeriod.isEmpty()) {
                    showInputErrorMessage(edlRentalPeriod, "rental period")
                    return@setOnClickListener
                } else {
                    edlRentalPeriod.isErrorEnabled = false
                }
                val ccNumber = edCcNumber.text.toString()
                if (ccNumber.isEmpty()) {
                    showInputErrorMessage(edlCcNumber, "cc number")
                    return@setOnClickListener
                } else {
                    edlCcNumber.isErrorEnabled = false
                }
                val secCode = edSecCode.text.toString()
                if (secCode.isEmpty()) {
                    showInputErrorMessage(edlSecCode, "security code")
                    return@setOnClickListener
                } else {
                    edlSecCode.isErrorEnabled = false
                }
                val expDate = edExpDate.text.toString()
                if (expDate.isEmpty()) {
                    showInputErrorMessage(edlExpDate, "expired date")
                    return@setOnClickListener
                } else {
                    edlExpDate.isErrorEnabled = false
                }
                cartViewModel.postFromCart(req)
            }
        }
    }

    private fun showAlert(response: InitialResponse<IdResponse>) {
        if (response.message == "OK") {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(getString(R.string.title_dialog_cart))
                setMessage(getString(R.string.message_dialog_cart))
                setPositiveButton(resources.getString(R.string.alert_ok)) { _, _ ->
                    finish()
                }
                create()
                show()
            }
        } else {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(resources.getString(R.string.cart_alert_title_error))
                setMessage(resources.getString(R.string.alert_error))
                setPositiveButton(resources.getString(R.string.alert_ok)) { _, _ ->
                }
                create()
                show()
            }
        }
    }

    private fun showInputErrorMessage(edl: TextInputLayout, name: String) {
        edl.error = String.format(getString(R.string.input_required_2), name)
    }

    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    private fun putCartDetail() {
        val deliveryItems = resources.getStringArray(R.array.delivery_items)
        val deliveryAdapter = ArrayAdapter(this, R.layout.list_item_delivery,deliveryItems)
        binding.edDelivery.setAdapter(deliveryAdapter)

        with (binding) {
            edRentalPeriod.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    val constraintsBuilder =
                        CalendarConstraints.Builder()
                            .setFirstDayOfWeek(Calendar.MONDAY)
                            .setValidator(DateValidatorPointForward.now())

                    val datePicker =
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select rental period")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .setCalendarConstraints(constraintsBuilder.build())
                            .build()

                    datePicker.show(supportFragmentManager,"RENTAL_PERIOD_DATE_PICKER")

                    datePicker.addOnPositiveButtonClickListener {
                        val cal = Calendar.getInstance()
                        cal.timeInMillis = it
                        val format = SimpleDateFormat("dd/MM/yyyy")
                        val formattedStartDate: String = format.format(cal.time)
                        req.rentalStartDate = cal.formatCalendarToISO8601()
                        val newCal = cal.clone() as Calendar
                        newCal.add(Calendar.DAY_OF_MONTH, 4)
                        val formattedEndDate: String = format.format(newCal.time)
                        req.rentalEndDate = newCal.formatCalendarToISO8601()
                        binding.edlRentalPeriod.editText?.setText("${formattedStartDate} - ${formattedEndDate}")
                    }
                }
                false
            }
            edExpDate.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    val constraintsBuilder =
                        CalendarConstraints.Builder()
                            .setFirstDayOfWeek(Calendar.MONDAY)
                            .setValidator(DateValidatorPointForward.now())

                    val datePicker =
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select expired date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .setCalendarConstraints(constraintsBuilder.build())
                            .build()

                    datePicker.show(supportFragmentManager, "EXP_DATE_PICKER")

                    datePicker.addOnPositiveButtonClickListener {
                        val cal = Calendar.getInstance()
                        cal.timeInMillis = it
                        val format = SimpleDateFormat("MM/yy")
                        val formattedDate: String = format.format(cal.time)
                        binding.edlExpDate.editText?.setText(formattedDate)
                    }
                }
                false
            }
            edDelivery.setOnItemClickListener { _, _, i, _ ->
                val selectedValue = deliveryAdapter.getItem(i)?.split("(")
                if (selectedValue != null) {
//                    req.deliveryMethod = selectedValue[0]
//                    req.deliveryPrice = selectedValue[1].currencyStringToInteger()
                }
            }
        }
    }

    private fun setCarts(products: List<ProductItem?>) {
        val adapter = CartAdapter()
        adapter.submitList(products)
        binding.rvCart.adapter = adapter
        binding.tvTotal.text = rentPrice.withCurrencyFormat()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val OWNER_NAME = "owner_name"
        const val RENT_PRICE = "rent_price"
        const val IMAGE_URL = "image_url"
        const val SIZE = "size"
        const val COLOR = "color"
    }
}