package com.amoure.amoure.ui.cart

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.afterTextChangedDelayed
import com.amoure.amoure.currencyStringToInteger
import com.amoure.amoure.data.request.PutCartRequest
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.ActivityCartBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat


class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel by viewModels<CartViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var req: PutCartRequest
    val regex = Regex("[^0-9]")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        // TODO: Remove
        req = PutCartRequest("", 1, "", "", "")

        cartViewModel.carts.observe(this) {
            // TODO: Change to CartResponse
            it?.products.let { products ->
                if (products != null) {
                    setCarts(products)
                }
            }
//            req = PutCartRequest(
//                it.delivery,
//                it.deliveryPrice,
//                it.cardNumber,
//                it.cardExpiry,
//                it.cardCVV,
//            )
//            with (binding) {
//                edCcNumber.setText(it.cardNumber)
//                edSecCode.setText(it.cardCVV)
//                edExpDate.setText(it.cardExpiry)
//                edDelivery.setText(String.format(getString(R.string.delivery_item), it.delivery, it.deliveryPrice?.withCurrencyFormat()), false)
//            }
        }

        cartViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        binding.topAppBarSecond.setNavigationOnClickListener {
            finish()
        }
        putCartDetail()
    }


    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    private fun putCartDetail() {
        val deliveryItems = resources.getStringArray(R.array.delivery_items)
        val deliveryAdapter = ArrayAdapter(this, R.layout.list_item_delivery,deliveryItems)
        binding.edDelivery.setAdapter(deliveryAdapter)

        with (binding) {
            edCcNumber.afterTextChangedDelayed {
                req.cardNumber = edCcNumber.text.toString()
                showToast(req.cardNumber)
//                cartViewModel.putFromCart(req)
            }
            edSecCode.afterTextChangedDelayed {
                req.cardCVV = edSecCode.text.toString()
                showToast(req.cardCVV)
//                cartViewModel.putFromCart(req)
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
//                cartViewModel.putFromCart(req)
                    }
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
        adapter.setOnItemClickCallback(object : CartAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                cartViewModel.deleteFromCart(id)
                val currentList = adapter.currentList.toMutableList()
                currentList.removeAt(currentList.indexOfFirst { it.id == id })
                adapter.submitList(currentList)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}