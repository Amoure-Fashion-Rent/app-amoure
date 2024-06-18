package com.amoure.amoure.ui.addproduct

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amoure.amoure.R
import com.amoure.amoure.data.request.PutProductRequest
import com.amoure.amoure.databinding.FragmentAddproductBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddproductBinding? = null
    private val binding get() = _binding!!
    private val addProductViewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddproductBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btAddProduct.setOnClickListener {
            putProduct()
        }

        addProductViewModel.isError.observe(viewLifecycleOwner) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        addProductViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun putProduct() {
        with(binding) {
            btAddProduct.setOnClickListener {
                val name = edProfileName.text.toString()
                if (name.isEmpty()) {
                    showInputErrorMessage(edlProfileName, "name")
                    return@setOnClickListener
                } else {
                    edlProfileName.isErrorEnabled = false
                }
                val product = edProfileProduct.text.toString()
                if (product.isEmpty()) {
                    showInputErrorMessage(edlProfileProduct, "product")
                    return@setOnClickListener
                } else {
                    edlProfileProduct.isErrorEnabled = false
                }
                val details = edProfileDetails.text.toString()
                if (details.isEmpty()) {
                    showInputErrorMessage(edlProfileDetails, "details")
                    return@setOnClickListener
                } else {
                    edlProfileDetails.isErrorEnabled = false
                }
                val notes = edProfileNotes.text.toString()
                if (notes.isEmpty()) {
                    showInputErrorMessage(edlProfileNotes, "notes")
                    return@setOnClickListener
                } else {
                    edlProfileNotes.isErrorEnabled = false
                }

                // Fetching Retail Price
                val retailPrice = edlRetailPrice.editText?.text.toString()
                if (retailPrice.isEmpty()) {
                    showInputErrorMessage(edlRetailPrice, "Retail Price")
                    return@setOnClickListener
                } else {
                    edlRetailPrice.isErrorEnabled = false
                }

                // Fetching Rent Price
                val rentPrice = edlRentPrice.editText?.text.toString()
                if (rentPrice.isEmpty()) {
                    showInputErrorMessage(edlRentPrice, "Rent Price")
                    return@setOnClickListener
                } else {
                    edlRentPrice.isErrorEnabled = false
                }

                val category = edCategory.text.toString()
                if (category.isEmpty()) {
                    showInputErrorMessage(edlCategory, "category")
                    return@setOnClickListener
                } else {
                    edlCategory.isErrorEnabled = false
                }

                // Collect selected sizes
                val selectedSizes = mutableListOf<String>()
                if (btSizeXs.isSelected) selectedSizes.add("XS")
                if (btSizeS.isSelected) selectedSizes.add("S")
                if (btSizeM.isSelected) selectedSizes.add("M")
                if (btSizeL.isSelected) selectedSizes.add("L")
                if (btSizeXl.isSelected) selectedSizes.add("XL")
                if (btSizeXxl.isSelected) selectedSizes.add("XXL")

//                finish()
            }
        }
    }

    private fun showInputErrorMessage(edl: TextInputLayout, name: String) {
        edl.error = String.format(getString(R.string.input_required_2), name)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
