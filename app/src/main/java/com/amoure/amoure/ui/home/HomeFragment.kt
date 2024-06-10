package com.amoure.amoure.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.FragmentHomeBinding
import com.amoure.amoure.ui.ProductMediumAdapter
import com.amoure.amoure.ui.ProductSmallAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.cart.CartActivity
import com.amoure.amoure.ui.search.SearchViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val searchViewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.rvTrending.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvForYou.layoutManager = GridLayoutManager(context, 2)

        homeViewModel.trendingProducts.observe(viewLifecycleOwner) {
            it?.let {
                setTrending(it)
            }
        }

        homeViewModel.forYouProducts.observe(viewLifecycleOwner) {
            it?.let {
                setForYou(it)
            }
        }

        homeViewModel.isError.observe(viewLifecycleOwner) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        setSearchBar()
        setTopAppBar()
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
//                        searchViewModel.getSearch(query)
                        svHome.removeAllViews()
                        val layoutInflater = LayoutInflater.from(context)
                        val layoutItem = layoutInflater.inflate(R.layout.item_search, null)
                        svHome.addView(layoutItem)
                    }
                    false
                }
        }
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
    }

    private fun setTrending(products: List<ProductItem?>) {
        val adapter = ProductSmallAdapter()
        adapter.submitList(products)
        binding.rvTrending.adapter = adapter
        adapter.setOnItemClickCallback(object : ProductSmallAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                // TODO: Go to product page
//                val moveIntent = Intent(context, DetailActivity::class.java)
//                moveIntent.putExtra(DetailActivity.ID, id)
//                startActivity(moveIntent)
            }
        })
    }

    private fun setForYou(products: List<ProductItem?>) {
        val adapter = ProductMediumAdapter()
        adapter.submitList(products)
        binding.rvForYou.adapter = adapter
        adapter.setOnItemClickCallback(object : ProductMediumAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                // TODO: Go to product page
//                val moveIntent = Intent(context, DetailActivity::class.java)
//                moveIntent.putExtra(DetailActivity.ID, id)
//                startActivity(moveIntent)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}