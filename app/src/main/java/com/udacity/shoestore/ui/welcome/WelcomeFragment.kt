package com.udacity.shoestore.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentWelcomeBinding
import com.udacity.shoestore.utils.SharedPreferencesHelper
import java.util.*


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialogSkip()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        setupEvents()
        return binding.root
    }

    private fun setupEvents() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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


    fun showDialogSkip() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.welcome_tittle))
            .setMessage(getString(R.string.welcome_description))
            .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}