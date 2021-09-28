package com.udacity.shoestore.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.shoestore.utils.SharedPreferencesHelper

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val error_username = "Username must be not empty"
    private val error_password = "Password must be not empty"

    private val _navigateOnBoarding = MutableLiveData<Boolean>()
    val navigateOnBoarding: LiveData<Boolean>
        get() = _navigateOnBoarding

    private val _navigateHome = MutableLiveData<Boolean>()
    val navigateHome: LiveData<Boolean>
        get() = _navigateHome

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun signIn(username: String, password: String) {
        when {
            username.isNullOrEmpty() -> {
                _error.value = error_username
            }
            password.isNullOrEmpty() -> {
                _error.value = error_password
            }
            validate(username, password) -> {
                if (SharedPreferencesHelper.getInstance().getUsername(this.getApplication())
                        .contentEquals(username)
                ) {

                    if (SharedPreferencesHelper.getInstance()
                            .showOnBoarding(this.getApplication())
                    ) {
                        _navigateOnBoarding.value = true
                    } else {
                        SharedPreferencesHelper.getInstance()
                            .setInSession(true, this.getApplication())
                        _navigateHome.value = true
                    }

                } else {
                    SharedPreferencesHelper.getInstance()
                        .setUsername(username, this.getApplication())

                    SharedPreferencesHelper.getInstance()
                        .setOnBoarding(true, this.getApplication())

                    _navigateOnBoarding.value = true
                }

            }
        }
    }

    private fun validate(username: String, password: String): Boolean {
        return true
    }
}