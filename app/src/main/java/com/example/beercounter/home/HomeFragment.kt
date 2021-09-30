package com.example.beercounter.home

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val arguments = HomeFragmentArgs.fromBundle(requireArguments())
        val database = BeerCounterDatabase.getInstance(application)
        val viewModelFactory = HomeViewModelFactory(database)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToAddBeer.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToAddBeerFragment(it))
                viewModel.onNavigatedToAddBeer()
            }
        })

        if (arguments.isBeerAdded) {
            addBeerCheckAnimation()
        }
        return binding.root
    }

    private fun addBeerCheckAnimation() {
        binding.beerImage.visibility = VISIBLE
        binding.check.visibility = VISIBLE
        var drawable =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                binding.check.drawable as AnimatedVectorDrawable
            } else {
                binding.check.drawable as AnimatedVectorDrawableCompat
            }
        drawable.start()
        val longestAnimationTime = 1500 //milliseconds, defined in XML

        binding.check.postDelayed(
            {
                binding.check.visibility = GONE
                binding.beerImage.visibility = GONE
            },
            longestAnimationTime.toLong()
        )
    }
}