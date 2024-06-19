package com.amoure.amoure.ui.addproduct

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.request.PostProductRequest
import com.amoure.amoure.databinding.FragmentAddproductBinding
import com.amoure.amoure.reduceFileImage
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.uriToFile
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddproductBinding? = null
    private val binding get() = _binding!!
    private val addProductViewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var galleryLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var imagesAdapter: ImageAddAdapter
    private var images = mutableListOf<Uri>()
    private var categories = mutableListOf<String>()
    private var categoryIds = mutableListOf<Int>()
    private var imagesToUpload = mutableListOf<MultipartBody.Part>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddproductBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(8)) { uris ->
            if (uris.isNotEmpty()) {
                images.addAll(uris)
                setImages()
            }
        }
        images.add("0".toUri())
        setImages()

        addProductViewModel.categoryResults.observe(viewLifecycleOwner) { list ->
            list?.let { iList ->
                categoryIds.clear()
                categories.clear()
                categoryIds.addAll(iList.mapNotNull { it?.id })
                categories.addAll(iList.mapNotNull { it?.name })

                context?.let {
                    val categoryAdapter = ArrayAdapter(it, R.layout.list_item_delivery, categories)
                    binding.edCategory.setAdapter(categoryAdapter)
                    val size = resources.getStringArray(R.array.size)
                    val sizeAdapter = ArrayAdapter(it, R.layout.list_item_delivery, size)
                    binding.edSize.setAdapter(sizeAdapter)
                }
            }
        }

        addProductViewModel.isError.observe(viewLifecycleOwner) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        addProductViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        postProduct()
    }

    private fun setImages() {
        binding.rvImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = ImageAddAdapter()
        imagesAdapter.submitList(images)
        binding.rvImages.adapter = imagesAdapter
        imagesAdapter.setOnItemClickCallback(object : ImageAddAdapter.OnItemClickCallback {
            override fun onItemClicked(flag: String) {
                if (flag == "0") {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
        })
    }

    private fun postImage(imageUri: Uri) {
        context?.let {
            val uri = uriToFile(imageUri, it).reduceFileImage()
            showLoading(true)

            val requestImageFile = uri.asRequestBody("image/jpeg".toMediaType())
            val image = MultipartBody.Part.createFormData(
                "photo",
                uri.name,
                requestImageFile
            )
            imagesToUpload.add(image)
        }
    }

    private fun postProduct() {
        with(binding) {
            btAddProduct.setOnClickListener {
                val product = edProfileProduct.text.toString()
                if (product.isEmpty()) {
                    showInputErrorMessage(edlProfileProduct, "product name")
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
                    showInputErrorMessage(edlProfileNotes, "stylish notes")
                    return@setOnClickListener
                } else {
                    edlProfileNotes.isErrorEnabled = false
                }
                val retailPrice = edRetailPrice.text.toString()
                if (retailPrice.isEmpty()) {
                    showInputErrorMessage(edlRetailPrice, "retail price")
                    return@setOnClickListener
                } else {
                    edlRetailPrice.isErrorEnabled = false
                }
                val rentPrice = edRentPrice.text.toString()
                if (rentPrice.isEmpty()) {
                    showInputErrorMessage(edlRentPrice, "rent price")
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

                val size = edSize.text.toString()
                if (size.isEmpty()) {
                    showInputErrorMessage(edlSize, "size")
                    return@setOnClickListener
                } else {
                    edlSize.isErrorEnabled = false
                }

                for (i in images.indices) {
                    postImage(images[i])
                }

                val productRequest = PostProductRequest(
                    product,
                    emptyList(),
                    details,
                    retailPrice.toInt(),
                    rentPrice.toInt(),
                    size,
                    "",
                    "AVAILABLE",
                    notes,
                    categoryIds[categories.indexOf(category)]
                )
                addProductViewModel.postProductWithImages(imagesToUpload, productRequest)
                images.clear()
                imagesToUpload.clear()
                images.add("0".toUri())
                imagesAdapter.submitList(images)
                edProfileProduct.setText("")
                edProfileDetails.setText("")
                edProfileNotes.setText("")
                edRetailPrice.setText("")
                edRentPrice.setText("")
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
