package com.amoure.amoure.ui.category

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
import com.amoure.amoure.data.response.CategoryItem
import com.amoure.amoure.databinding.FragmentCategoryBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.search.SearchFragment

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

        setSearchBar()
        return root
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
                        val bundle = Bundle()
                        bundle.putString(SearchFragment.QUERY, query)
                        findNavController().navigate(R.id.action_navigation_home_to_navigation_search, bundle)
                    }
                    false
                }
        }
    }

    private fun setCategoryResults(products: List<CategoryItem?>) {
        val adapter = CategoryAdapter()
        adapter.submitList(products)
        binding.rvCategory.adapter = adapter
        adapter.setOnItemClickCallback(object : CategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val bundle = Bundle()
                bundle.putInt(CategoryClickFragment.ID, id)
                findNavController().navigate(R.id.action_navigation_category_to_navigation_category_click, bundle)
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