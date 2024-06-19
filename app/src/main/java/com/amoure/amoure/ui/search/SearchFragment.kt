package com.amoure.amoure.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.FragmentSearchBinding
import com.amoure.amoure.reduceFileImage
import com.amoure.amoure.ui.ProductMedium2Adapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.product.ProductActivity
import com.amoure.amoure.uriToFile
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val searchViewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!
    private var query: String? = ""
    private var isVisSearch: Boolean? = false
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.rvSearch.layoutManager = GridLayoutManager(context, 2)

        query = arguments?.getString(QUERY)
        isVisSearch = arguments?.getBoolean(IS_VIS_SEARCH)
        imageUri = arguments?.getString(IMAGE_URI)?.toUri()
        query?.let {
            binding.itemProductSmall.visibility = View.GONE
            searchViewModel.getSearch(it)
        }
        imageUri?.let {
//            binding.itemProductSmall.visibility = View.GONE
            binding.itemProductSmall.visibility = View.VISIBLE
            Glide.with(binding.ivProduct.context)
                .load(imageUri)
                .into(binding.ivProduct)
//            postSearchVis(it)
        }

        searchViewModel.searchResults.observe(viewLifecycleOwner) {
            it?.let {
                setSearchResults(it)
            }
            if (isVisSearch == true) {
                binding.itemProductSmall.visibility = View.VISIBLE
                Glide.with(binding.ivProduct.context)
                    .load(imageUri)
                    .into(binding.ivProduct)
                // TODO: Get prediction category
                binding.tvVisSearch.text = String.format(getString(R.string.vis_search_message), "")
                binding.tvVisSearch.setOnClickListener {
                    findNavController().navigate(R.id.navigation_category_click)
                }
            }
        }

        searchViewModel.isLoading.observe(viewLifecycleOwner) {
            it?.let {
                showLoading(it)
            }
        }

        setSearchBar()
        setTopAppBar()
        return root
    }

    private fun setSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchBar.setText(query)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    if (searchView.text.toString() != "") {
                        val query = searchView.text.toString()
                        searchViewModel.getSearch(query)
                    }
                    false
                }
        }
    }

    private fun setTopAppBar() {
        binding.searchBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun postSearchVis(imageUri: Uri) {
        context?.let {
            val imageFile = uriToFile(imageUri, it).reduceFileImage()
            showLoading(true)

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            searchViewModel.postVisSearch(multipartBody)
        }
    }

    private fun setSearchResults(products: List<ProductItem?>) {
        val adapter = ProductMedium2Adapter()
        binding.rvSearch.adapter = adapter
        adapter.submitList(products)
        adapter.setOnItemClickCallback(object : ProductMedium2Adapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val moveIntent = Intent(context, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id.toString())
                startActivity(moveIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val QUERY = "query"
        const val IS_VIS_SEARCH = "is_vis_search"
        const val IMAGE_URI = "image_uri"
    }
}