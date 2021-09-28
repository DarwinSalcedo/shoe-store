package com.udacity.shoestore.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentCreateShoeBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.ui.list.ListViewModel
import com.udacity.shoestore.ui.list.ViewModelFactory

class CreateShoeFragment : Fragment() {

    private lateinit var binding: FragmentCreateShoeBinding
    private lateinit var viewModel: ListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance()).get(ListViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_shoe, container, false)
        setEvents()
        return binding.root
    }

    private fun setEvents() {
        binding.createButton.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val size =
                if (binding.size.text.toString().trim()
                        .isEmpty()
                ) 0.0 else binding.size.text.toString()
                    .toDouble()
            val company = binding.company.text.toString().trim()
            val description = binding.description.text.toString().trim()
            val imagesUri = binding.image.text.toString().trim()
            val images =
                listOf(imagesUri)
            viewModel.createShoe(Shoe(name, size, company, description, images))
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

}
