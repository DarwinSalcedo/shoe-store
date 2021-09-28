package com.udacity.shoestore.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.ui.detail.DetailViewModel
import com.udacity.shoestore.ui.list.ListViewModel

class ViewModelFactory(private val shoe: Shoe? = null) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ListViewModel::class.java) -> ListViewModel.getInstance()
                isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(shoe)
                else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
            }
        } as T


    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance() =
            instance ?: synchronized(ViewModelFactory::class.java) {
                instance ?: ViewModelFactory().also { instance = it }
            }
    }
}