package com.udacity.shoestore.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentDetailBinding
import com.udacity.shoestore.ui.list.ViewModelFactory

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val item = DetailFragmentArgs.fromBundle(requireArguments()).shoe
        viewModel =
            ViewModelProvider(this, ViewModelFactory(item)).get(DetailViewModel::class.java)

        binding.viewModel = viewModel
        item.images.takeIf { it.isNotEmpty() }?.let {
            bindImage(binding.image, it.first())
        }
        return binding.root

    }

    private fun bindImage(imgView: ImageView, imgUrl: String) {
        imgUrl.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUri)
                .error(R.drawable.ic_broken_image)
                .into(imgView)
        }
    }

}