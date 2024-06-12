package com.amoure.amoure.ui.editprofile

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amoure.amoure.R
import com.amoure.amoure.data.request.PutProfileRequest
import com.amoure.amoure.data.response.Profile
import com.amoure.amoure.databinding.ActivityEditProfileBinding
import com.amoure.amoure.isEmailValid
import com.amoure.amoure.ui.ViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val editProfileViewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userType: String

    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putProfile()

        editProfileViewModel.profile.observe(this) {
            setProfile(it)
        }

        editProfileViewModel.isError.observe(this) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        editProfileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.topAppBarSecond.setNavigationOnClickListener {
            finish()
        }

        binding.edProfileBirthDate.setOnTouchListener { _, motionEvent ->
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

                datePicker.show(supportFragmentManager, "BIRTH_DATE_PICKER")

                datePicker.addOnPositiveButtonClickListener {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = it
                    val format = SimpleDateFormat("dd/MM/yyyy")
                    val formattedDate: String = format.format(cal.time)
                    binding.edlProfileBirthDate.editText?.setText(formattedDate)
                }
            }
            false
        }

    }

    private fun setProfile(profile: Profile) {
        with(binding) {
            userType = profile.userType.toString()
            if (profile.userType == "owner") {
                tvProfileName.text = getString(R.string.designer_name)
                tvProfileBirthDate.visibility = View.GONE
                edlProfileBirthDate.visibility = View.GONE
                edProfileBirthDate.visibility = View.GONE
            }
            edProfileName.setText(profile.fullName)
            edProfileEmail.setText(profile.email)
            edProfileProvince.setText(profile.province)
            edProfileCity.setText(profile.city)
            edProfileDistrict.setText(profile.district)
            edProfilePostalCode.setText(profile.postalCode)
            edProfileAddress.setText(profile.addressDetail)
            edProfilePhoneNum.setText(profile.phoneNumber)
            edProfileBirthDate.setText(profile.birthDate)
        }
    }

    private fun putProfile() {
        with(binding) {
            btSave.setOnClickListener {
                val name = edProfileName.text.toString()
                val email = edProfileEmail.text.toString()
                if (email.isEmpty() || !isEmailValid(email)) {
                    edProfileEmail.error = String.format(getString(R.string.input_required), "email")
                }
                val province = edProfileProvince.text.toString()
                val city = edProfileCity.text.toString()
                val district = edProfileDistrict.text.toString()
                val postalCode = edProfilePostalCode.text.toString()
                val address = edProfileAddress.text.toString()
                val phoneNum = edProfilePhoneNum.text.toString()
                val birthDate = edProfileBirthDate.text.toString()

                if (userType == "owner") {
                    editProfileViewModel.putProfile(PutProfileRequest(name, email, province, city, district, postalCode, address, phoneNum))
                } else {
                    editProfileViewModel.putProfile(PutProfileRequest(name, email, province, city, district, postalCode, address, phoneNum, birthDate))
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}