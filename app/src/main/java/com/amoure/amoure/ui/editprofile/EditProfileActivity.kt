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
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.Profile
import com.amoure.amoure.databinding.ActivityEditProfileBinding
import com.amoure.amoure.isPhoneNumberValid
import com.amoure.amoure.ui.ViewModelFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
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

        editProfileViewModel.response.observe(this) {
            showAlert(it)
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
            if (profile.userType == "OWNER") {
                tvProfileName.text = getString(R.string.designer_name)
                tvProfileBirthDate.visibility = View.GONE
                edlProfileBirthDate.visibility = View.GONE
                edProfileBirthDate.visibility = View.GONE
            }
            edProfileName.setText(profile.fullName)
            edProfileProvince.setText(profile.province)
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
                if (name.isEmpty()) {
                    showInputErrorMessage(edlProfileName, "name")
                    return@setOnClickListener
                } else {
                    edlProfileName.isErrorEnabled = false
                }
                val province = edProfileProvince.text.toString()
                if (province.isEmpty()) {
                    showInputErrorMessage(edlProfileProvince, "province")
                } else {
                    edlProfileProvince.isErrorEnabled = false
                }
                val district = edProfileDistrict.text.toString()
                if (district.isEmpty()) {
                    showInputErrorMessage(edlProfileDistrict, "district")
                    return@setOnClickListener
                } else {
                    edlProfileDistrict.isErrorEnabled = false
                }
                val postalCode = edProfilePostalCode.text.toString()
                if (postalCode.isEmpty()) {
                    showInputErrorMessage(edlProfilePostalCode, "postal code")
                    return@setOnClickListener
                } else {
                    edlProfilePostalCode.isErrorEnabled = false
                }
                val address = edProfileAddress.text.toString()
                if (address.isEmpty()) {
                    showInputErrorMessage(edlProfileAddress, "address")
                    return@setOnClickListener
                } else {
                    edlProfileAddress.isErrorEnabled = false
                }
                val phoneNum = edProfilePhoneNum.text.toString()
                if (phoneNum.isEmpty() || !isPhoneNumberValid(phoneNum)) {
                    edlProfilePhoneNum.error = String.format(getString(R.string.input_required), "phone number")
                    return@setOnClickListener
                } else {
                    edlProfilePhoneNum.isErrorEnabled = false
                }
                val birthDate = edProfileBirthDate.text.toString()
                if (birthDate.isEmpty()) {
                    showInputErrorMessage(edlProfileBirthDate, "birth date")
                    return@setOnClickListener
                } else {
                    edlProfileBirthDate.isErrorEnabled = false
                }

                if (userType == "OWNER") {
                    editProfileViewModel.putProfile(PutProfileRequest(name, province, district, postalCode, address, phoneNum))
                } else {
                    editProfileViewModel.putProfile(PutProfileRequest(name, province, district, postalCode, address, phoneNum, birthDate))
                }
            }
        }
    }

    private fun showAlert(response: InitialResponse<IdResponse>) {
        if (response.message == "OK") {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(getString(R.string.title_dialog_edit_profile))
                setPositiveButton(resources.getString(R.string.alert_ok)) { _, _ ->
                    finish()
                }
                create()
                show()
            }
        } else {
            MaterialAlertDialogBuilder(this).apply {
                setTitle(resources.getString(R.string.edit_profile_alert_title_error))
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}