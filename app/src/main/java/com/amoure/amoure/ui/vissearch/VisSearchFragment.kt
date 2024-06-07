package com.amoure.amoure.ui.vissearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amoure.amoure.R
import com.amoure.amoure.databinding.FragmentVisSearchBinding
import com.amoure.amoure.ui.cart.CartActivity

class VisSearchFragment : Fragment() {

    private var _binding: FragmentVisSearchBinding? = null
    private val visSearchViewModel by viewModels<VisSearchViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textVisSearch
        visSearchViewModel.text.observe(viewLifecycleOwner) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}