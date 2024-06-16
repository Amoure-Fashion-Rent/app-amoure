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
import com.amoure.amoure.afterTextChangedDelayed
import com.amoure.amoure.currencyStringToInteger
import com.amoure.amoure.data.request.PutCartRequest
import com.amoure.amoure.data.response.Owner
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ActivityCartBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.withCurrencyFormat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import kotlin.properties.Delegates


class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var req: PutCartRequest
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
        product = ProductItem(owner = Owner(fullName = ownerName), id = productId, rentPrice = rentPrice, name = productName, images = listOf(imageUrl))

        // TODO: Remove
        req = PutCartRequest("","", 4, "", 1, "", "", "")

        cartViewModel.carts.observe(this) {
            // TODO: match with backend
//            req = PutCartRequest(
//                it.delivery,
//                it.deliveryPrice,
//                it.cardNumber,
//                it.cardExpiry,
//                it.cardCVV,
//            )
//            with (binding) {
//                edDelivery.setText(String.format(getString(R.string.delivery_item), it.delivery, it.deliveryPrice?.withCurrencyFormat()), false)
//            }
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
    }

    private fun postCart() {
        with(binding) {
            btCheckout.setOnClickListener {
                // TODO: match with backend
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
                // req.delivery, req.deliveryPrice, address, productId
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
                        val formattedDate: String = format.format(cal.time)
                        binding.edlRentalPeriod.editText?.setText(formattedDate)
                    }
                }
                false
            }
            edCcNumber.afterTextChangedDelayed {
                req.cardNumber = edCcNumber.text.toString()
                showToast(req.cardNumber)
            }
            edSecCode.afterTextChangedDelayed {
                req.cardCVV = edSecCode.text.toString()
                showToast(req.cardCVV)
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
                        req.cardExpiry = formattedDate
                        showToast(req.cardExpiry)
                    }
//                cartViewModel.putFromCart(req)
                }
                false
            }
            edDelivery.setOnItemClickListener { _, _, i, _ ->
                val selectedValue = deliveryAdapter.getItem(i)?.split("(")
                if (selectedValue != null) {
                    req.delivery = selectedValue[0]
                    req.deliveryPrice = selectedValue[1].currencyStringToInteger()
                }
//                cartViewModel.putFromCart(req)
                showToast(req.delivery + req.deliveryPrice.toString())
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
    }
}