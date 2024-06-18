package com.amoure.amoure.ui.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.Wishlist
import com.amoure.amoure.databinding.FragmentWishlistBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.cart.CartActivity
import com.amoure.amoure.ui.cart.CartAdapter

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val wishListViewModel by viewModels<WishlistViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.rvWishlist.layoutManager = GridLayoutManager(context, 2)

        wishListViewModel.wishlist.observe(viewLifecycleOwner) {
        }

        wishListViewModel.products.observe(viewLifecycleOwner) {
            it?.let {
                SetWishlistResults(it)
            }
        }


        wishListViewModel.isError.observe(viewLifecycleOwner) {
            if (it == true) showToast(resources.getString(R.string.alert_error))
        }

        wishListViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        return root
    }



    private fun SetWishlistResults(products: List<ProductItem?>) {
        val adapter = WishlistAdapter()
        adapter.submitList(products)
        binding.rvWishlist.adapter = adapter
        adapter.setOnItemClickCallback(object : WishlistAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                wishListViewModel.deleteFromWishlist(id)
                val currentList = adapter.currentList.toMutableList()
                currentList.removeAt(currentList.indexOfFirst { it.id == id })
                adapter.submitList(currentList)
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