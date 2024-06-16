package com.amoure.amoure.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.databinding.FragmentHomeBinding
import com.amoure.amoure.getDummyProducts
import com.amoure.amoure.ui.ProductMediumAdapter
import com.amoure.amoure.ui.ProductSmallAdapter
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.product.ProductActivity
import com.amoure.amoure.ui.search.SearchFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel> {
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
        // TODO: Remove!
        setTrending(getDummyProducts())
        setForYou(getDummyProducts())

        homeViewModel.forYouProducts.observe(viewLifecycleOwner) {
            it?.let {
                setForYou(it)
            }
        }

        homeViewModel.isError.observe(viewLifecycleOwner) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
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

    private fun setTrending(products: List<ProductItem?>) {
        val adapter = ProductSmallAdapter()
        adapter.submitList(products)
        binding.rvTrending.adapter = adapter
        adapter.setOnItemClickCallback(object : ProductSmallAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                val moveIntent = Intent(context, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id)
                startActivity(moveIntent)
            }
        })
    }

    private fun setForYou(products: List<ProductItem?>) {
        val adapter = ProductMediumAdapter()
        adapter.submitList(products)
        binding.rvForYou.adapter = adapter
        adapter.setOnItemClickCallback(object : ProductMediumAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                val moveIntent = Intent(context, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id)
                startActivity(moveIntent)
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