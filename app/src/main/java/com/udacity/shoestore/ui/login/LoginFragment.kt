package com.udacity.shoestore.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        setupObserver()
        setupEvents()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.navigateOnBoarding.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_loginFragment_to_welcomeFragment)
            }
        }

        viewModel.navigateHome.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            with(binding.signInError) {
                visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                text = it
            }
        }

    }

    private fun setupEvents() {

        val textWatcher = object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                binding.signInButton.isEnabled =
                    binding.signinUsernameInput.text.toString().trim().isNotEmpty()
                            && binding.signinPasswordInput.text.toString().trim().isNotEmpty()
            }
        }

        binding.signInButton.setOnClickListener {
            onNextPressed()
        }

        binding.signinUsernameInput.addTextChangedListener(textWatcher)
        binding.signinPasswordInput.addTextChangedListener(textWatcher)


        binding.signinUsernameInput.doAfterTextChanged {
            binding.signinUsernameContainer.error = null
        }

        binding.signinPasswordInput.doAfterTextChanged {
            binding.signinPasswordContainer.error = null
        }
    }

    private fun onNextPressed() {
        var hasError = true

        if (binding.signinUsernameInput.text.toString().trim().isNullOrEmpty()) {
            binding.signInButton.isEnabled = false
            binding.signinUsernameContainer.error =
                getString(R.string.login_empty_username)
            hasError = false
        }
        if (binding.signinPasswordInput.text.toString().trim().isNullOrEmpty()) {
            binding.signInButton.isEnabled = false
            binding.signinPasswordContainer.error =
                getString(R.string.login_empty_passwword)
            hasError = false
        }

        if (hasError) {
            viewModel.signIn(
                binding.signinUsernameInput.text.toString().trim(),
                binding.signinPasswordInput.text.toString().trim()
            )
        }
    }

}