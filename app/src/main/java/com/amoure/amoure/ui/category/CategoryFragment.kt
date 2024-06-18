package com.amoure.amoure.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.FragmentCategoryBinding
import com.google.android.material.shape.MaterialShapeDrawable
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.cart.CartActivity

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val categoryViewModel by viewModels<CategoryViewModel>() {
        ViewModelFactory.getInstance(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvCategory.layoutManager = GridLayoutManager(context, 2)

        categoryViewModel.categoryResults.observe(viewLifecycleOwner) {
            it?.let {
                setCategoryResults(it)
            }
        }

        categoryViewModel.isError.observe(viewLifecycleOwner) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        categoryViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(activity)

        setSearchBar()
        setTopAppBar()
        return root
    }

    private fun setTopAppBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cart -> {
                    val moveIntent = Intent(context, CartActivity::class.java)
                    startActivity(moveIntent)
                    true
                }
                else -> false
            }
        }
        binding.searchBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    if (searchView.text.toString() != "") {
                        val query = searchView.text.toString()
//                        searchViewModel.getSearch(query)
                    }
                    false
                }
        }
    }

    private fun setCategoryResults(products: List<ProductItem?>) {
        val adapter = CategoryAdapter()
        adapter.submitList(products)
        binding.rvCategory.adapter = adapter
        adapter.setOnItemClickCallback(object : CategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                // TODO: Go to list of category lists
//                val moveIntent = Intent(context, DetailActivity::class.java)
//                moveIntent.putExtra(DetailActivity.ID, id)
//                startActivity(moveIntent)
            }
        })
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