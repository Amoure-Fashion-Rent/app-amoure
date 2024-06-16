package com.amoure.amoure.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amoure.amoure.databinding.FragmentProfileBinding
import com.amoure.amoure.ui.ViewModelFactory
import com.amoure.amoure.ui.editprofile.EditProfileActivity
import com.amoure.amoure.ui.renthistory.RentHistoryActivity
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

        setupAction()
        return root
    }

    private fun setupAction() {
        with(binding) {
            btEditProfile.setOnClickListener {
                val moveIntent = Intent(context, EditProfileActivity::class.java)
                startActivity(moveIntent)
            }
            btYourRent.setOnClickListener {
                val moveIntent = Intent(context, RentHistoryActivity::class.java)
                startActivity(moveIntent)
            }
            btLogout.setOnClickListener {
                profileViewModel.logout()
                val intent = Intent(context, StartActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}