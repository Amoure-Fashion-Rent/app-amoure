package com.amoure.amoure.ui.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amoure.amoure.R
import com.amoure.amoure.databinding.FragmentWishlistBinding
import com.amoure.amoure.ui.cart.CartActivity

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val wishListViewModel by viewModels<WishlistViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textWishlist
        wishListViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
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
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}