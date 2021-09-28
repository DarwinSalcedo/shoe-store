package com.udacity.shoestore.ui.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.utils.SharedPreferencesHelper

class ListViewModel() : ViewModel() {
    private var dataCache = mutableListOf<Shoe>()
    private val _listOfShoe = MutableLiveData<List<Shoe>>().apply { value = dataCache.toList() }
    val listOfShoe: LiveData<List<Shoe>> = _listOfShoe

    private val _session = MutableLiveData<Boolean>().apply { value = false }
    val session: LiveData<Boolean> = _session


    fun createShoe(shoe: Shoe) {
        dataCache.add(shoe)
        _listOfShoe.value = dataCache.toList()
    }

    fun checkSession(context: Context) {

        _session.value =
            SharedPreferencesHelper.getInstance().getInSession(context)

    }

    companion object {
        private var instance: ListViewModel? = null
        fun getInstance() =
            instance ?: synchronized(ListViewModel::class.java) {
                instance ?: ListViewModel().also { instance = it }
            }
    }
}