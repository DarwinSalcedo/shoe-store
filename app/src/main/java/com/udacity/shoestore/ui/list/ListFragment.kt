package com.udacity.shoestore.ui.list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentListBinding
import com.udacity.shoestore.databinding.ItemShoeHomeBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.utils.SharedPreferencesHelper
import java.util.*

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    private lateinit var adapter: ShoeAdapter
    private val callback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialogLogout()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance()).get(ListViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        initView()
        setEvents()
        addObservers()
        return binding.root
    }

    private fun addObservers() {
        viewModel.checkSession(requireContext())

        viewModel.listOfShoe.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                showResults(false)
            } else {
                adapter.updateList(list)
                showResults(true)
            }
        }

        viewModel.session.observe(viewLifecycleOwner) {
            if (it) {
                binding.sessionStastus.text = getString(R.string.button_logout)
                binding.sessionStastus.setOnClickListener { showDialogLogout() }
            } else {
                binding.sessionStastus.text = getString(R.string.button_log_in)
                binding.sessionStastus.setOnClickListener {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_homeFragment_to_loginFragment)
                }
            }
        }
    }

    private fun showResults(enabled: Boolean) {
        if (enabled) {
            binding.shoeList.visibility = View.VISIBLE
            binding.statusData.visibility = View.GONE
        } else {
            binding.shoeList.visibility = View.GONE
            binding.statusData.visibility = View.VISIBLE
        }

    }

    private fun initView() {
        adapter = ShoeAdapter { item -> showDetail(item) }
        binding.shoeList.adapter = adapter
    }

    private fun showDetail(item: Shoe) {
        Navigation.findNavController(binding.root).navigate(
            ListFragmentDirections.actionHomeFragmentToDetailFragment(item)
        )
    }

    private fun setEvents() {

        binding.buttonAddNewOne.setOnClickListener {
            SharedPreferencesHelper.getInstance().getInSession(requireContext()).let {
                if (it) {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_homeFragment_to_createShoeFragment)
                }
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    fun showDialogLogout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.button_logout))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
                dialog.dismiss()
                SharedPreferencesHelper.getInstance().setInSession(false, requireContext())
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_homeFragment_to_loginFragment)
            }
            .show()
    }

    class ShoeAdapter(var data: List<Shoe> = emptyList(), val callback: (Shoe) -> Unit) :
        RecyclerView.Adapter<ShoeViewHolder>() {

        @SuppressLint("NotifyDataSetChanged")
        fun updateList(_data: List<Shoe>) {
            data = _data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShoeViewHolder {
            return ShoeViewHolder.from(parent)
        }

        override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
            data[position].let { item ->
                holder.bind(item, callback)
            }
        }

        override fun getItemCount(): Int = data.size
    }

    class ShoeViewHolder private constructor(
        private val binding: ItemShoeHomeBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            data: Shoe,
            callback: (Shoe) -> Unit
        ) {
            with(binding) {
                name.text = data.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                company.text = data.company.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                size.text = String.format(context.getString(R.string.size_format), data.size.toString())
                goDetail.setOnClickListener { callback(data) }
                data.images.takeIf { it.isNotEmpty() }?.let {
                    bindImage(mainImage, it.first())
                }
            }
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


        companion object {
            fun from(parent: ViewGroup): ShoeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemShoeHomeBinding.inflate(layoutInflater, parent, false)
                return ShoeViewHolder(binding, parent.context)
            }
        }
    }
}