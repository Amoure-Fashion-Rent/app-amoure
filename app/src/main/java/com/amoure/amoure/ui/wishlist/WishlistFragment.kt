package com.amoure.amoure.ui.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amoure.amoure.R
import com.amoure.amoure.data.response.WishlistItem
import com.amoure.amoure.databinding.FragmentWishlistBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.product.ProductActivity

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
        binding.rvWishlist.layoutManager = LinearLayoutManager(context)

        wishListViewModel.products.observe(viewLifecycleOwner) {
            it?.let {
                setWishlistResults(it)
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



    private fun setWishlistResults(products: List<WishlistItem?>) {
        if (products.isEmpty()) {
            binding.tvNoDataRent.isVisible = true
        }
        val adapter = WishlistAdapter()
        adapter.submitList(products)
        binding.rvWishlist.adapter = adapter
        adapter.setOnItemClickCallback(object : WishlistAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val moveIntent = Intent(context, ProductActivity::class.java)
                moveIntent.putExtra(ProductActivity.ID, id.toString())
                startActivity(moveIntent)
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