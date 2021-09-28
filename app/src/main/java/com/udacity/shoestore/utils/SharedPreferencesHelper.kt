package com.udacity.shoestore.utils

import android.content.Context

class SharedPreferencesHelper {
    private val file = "shoe_store_data"
    private val field_username = "username"
    private val field_onboarding = "endOnBoarding"
    private val field_in_session = "inSession"

    fun setUsername(username: String, context: Context) {
        val sharedPref = sharedPreferences(context)
        with(sharedPref.edit()) {
            putString(field_username, username)
            apply()
        }
    }

    fun getUsername(context: Context): String {
        val sharedPref = sharedPreferences(context)
        val username = sharedPref.getString(field_username, "")
        return username ?: ""
    }

    fun setOnBoarding(end: Boolean, context: Context) {
        val sharedPref = sharedPreferences(context)
        with(sharedPref.edit()) {
            putBoolean(field_onboarding, end)
            apply()
        }
    }

    fun showOnBoarding(context: Context): Boolean {
        val sharedPref = sharedPreferences(context)
        return sharedPref.getBoolean(field_onboarding, true)
    }

    fun setInSession(end: Boolean, context: Context) {
        val sharedPref = sharedPreferences(context)
        with(sharedPref.edit()) {
            putBoolean(field_in_session, end)
            apply()
        }
    }
    fun getInSession(context: Context): Boolean {
        val sharedPref = sharedPreferences(context)
        return sharedPref.getBoolean(field_in_session, false)
    }
    private fun sharedPreferences(context: Context) = context.getSharedPreferences(
        file, Context.MODE_PRIVATE
    )


    companion object {
        private var instance: SharedPreferencesHelper? = null
        fun getInstance() =
            instance ?: synchronized(SharedPreferencesHelper::class.java) {
                instance ?: SharedPreferencesHelper().also { instance = it }
            }
    }
}