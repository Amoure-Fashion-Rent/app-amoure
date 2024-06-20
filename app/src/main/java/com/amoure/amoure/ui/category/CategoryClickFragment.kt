package com.amoure.amoure.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.databinding.FragmentCategoryClickBinding
import com.amoure.amoure.ui.LoadingStateAdapter
import com.amoure.amoure.ui.ProductMediumAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.product.ProductActivity
import com.amoure.amoure.ui.search.SearchFragment
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoryClickFragment : Fragment() {

    private var _binding: FragmentCategoryClickBinding? = null
    private val categoryViewModel by viewModels<CategoryViewModel>() {
        ViewModelFactory.getInstance(requireContext())
    }
    private var categoryId by Delegates.notNull<Int>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryClickBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvCategory.layoutManager = GridLayoutManager(context, 2)

        val idTemp = arguments?.getInt(ID)
        idTemp?.let { categoryId = it }

        categoryViewModel.getProductByCategory(categoryId)

        setProduct()
        setSearchBar()
        setTopAppBar()
        return root
    }

    private fun setTopAppBar() {
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
                        val bundle = Bundle()
                        bundle.putString(SearchFragment.QUERY, query)
                        findNavController().navigate(R.id.action_navigation_category_click_to_navigation_search, bundle)
                    }
                    false
                }
        }
    }

    private fun setProduct() {
        val adapter = ProductMediumAdapter()
        binding.rvCategory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }
        categoryViewModel.productsCategory.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitData(lifecycle, it)
            }
        }
        adapter.setOnItemClickCallback(object : ProductMediumAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val moveIntent = Intent(context, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id.toString())
                startActivity(moveIntent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val ID = "id"
    }
}