package com.amoure.amoure.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amoure.amoure.R
import com.amoure.amoure.databinding.FragmentProfileBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.cart.CartActivity
import com.amoure.amoure.ui.start.StartActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setTopAppBar()
        setupAction()
        return root
    }

    private fun setupAction() {
        with(binding) {
            btEditProfile.setOnClickListener {
                // TODO: Go to edit profile page
//                val moveIntent = Intent(context, DetailActivity::class.java)
//                moveIntent.putExtra(DetailActivity.ID, id)
//                startActivity(moveIntent)
            }
            btYourRent.setOnClickListener {
                // TODO: Go to your rent page
//                val moveIntent = Intent(context, DetailActivity::class.java)
//                moveIntent.putExtra(DetailActivity.ID, id)
//                startActivity(moveIntent)
            }
            btLogout.setOnClickListener {
                profileViewModel.logout()
                val intent = Intent(requireContext(), StartActivity::class.java)
                startActivity(intent)
                activity?.finish()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}