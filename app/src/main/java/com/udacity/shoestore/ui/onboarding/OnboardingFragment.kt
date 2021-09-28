package com.udacity.shoestore.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentOnboardingBinding
import com.udacity.shoestore.databinding.ItemSlideOnboardingBinding
import com.udacity.shoestore.models.Slide
import com.udacity.shoestore.utils.SharedPreferencesHelper

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)
        prepareSlides()
        return binding.root
    }

    private fun onboardingSteps(): List<Slide> {
        val step1 = Slide("Create", "", "Create shoes easily, on simple steps.")
        val step2 = Slide("Details", "", "We can see details of each one")
        val step3 = Slide("List of shoes", "", "You can browse faster on it")
        val step4 = Slide("Let's go!", "", "Here we go")
        return listOf(step1, step2, step3, step4)
    }

    private fun prepareSlides() {
        binding.viewPager.apply {
              offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                clipToPadding = false
            }
            adapter = SlideAdapter(onboardingSteps()) { navigate() }
        }
        binding.indicator.setViewPager(binding.viewPager)
    }

    private fun navigate() {
        SharedPreferencesHelper.getInstance().setOnBoarding(false, requireContext())
        SharedPreferencesHelper.getInstance().setInSession(true,requireContext())
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_onboardingFragment_to_homeFragment)
    }

    class SlideAdapter(val screens: List<Slide>, val callback: () -> Unit) :
        RecyclerView.Adapter<SlideViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
            return SlideViewHolder.from(parent)
        }

        override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
            screens[position].let { slide ->
                holder.bind(screens.lastIndex == position, slide, callback)
            }
        }

        override fun getItemCount(): Int = screens.size
    }

    class SlideViewHolder private constructor(
        private val binding: ItemSlideOnboardingBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            isLast: Boolean,
            data: Slide,
            callback: () -> Unit
        ) {
            with(binding) {
                title.text = data.title
                subTitle.text = data.subTitle
                mainDescription.text = data.mainDescription
                if (isLast) {
                    continueButton.visibility = View.VISIBLE
                    continueButton.setOnClickListener { callback() }
                } else {
                    continueButton.visibility = View.GONE
                    continueButton.setOnClickListener { }
                }

            }
        }

        companion object {
            fun from(parent: ViewGroup): SlideViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemSlideOnboardingBinding.inflate(layoutInflater, parent, false)
                return SlideViewHolder(binding, parent.context)
            }
        }
    }
}