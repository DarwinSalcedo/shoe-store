package com.udacity.shoestore.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentWelcomeBinding
import com.udacity.shoestore.utils.SharedPreferencesHelper
import java.util.*


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        setupEvents()
        return binding.root
    }

    private fun setupEvents() {
        val username = SharedPreferencesHelper.getInstance().getUsername(requireContext())
        binding.welcome.text = String.format(
            getString(R.string.welcome_title_onboarding), username.capitalize(
                Locale.getDefault()
            )
        )
        binding.continueButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_welcomeFragment_to_onboardingFragment)
        }
    }

}