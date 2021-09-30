package com.example.beercounter.addbeer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.beercounter.R
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.databinding.FragmentAddBeerBinding


class AddBeerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddBeerBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val arguments = AddBeerFragmentArgs.fromBundle(requireArguments())
        val database = BeerCounterDatabase.getInstance(application)
        val viewModelFactory = AddBeerViewModelFactory(arguments.sessionId, database, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(AddBeerViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToHome.observe(viewLifecycleOwner, {
            if (it) {
                val navigator = AddBeerFragmentDirections.actionAddBeerFragmentToHomeFragment()
                findNavController()
                    .navigate(navigator)
                viewModel.onNavigatedToHome()
            }
        })

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    val navigator = AddBeerFragmentDirections.actionAddBeerFragmentToHomeFragment().setIsBeerAdded(false)
                    findNavController().navigate(navigator)
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback);

        createChannel(
            getString(R.string.beer_notification_channel_id),
            getString(R.string.beer_notification_channel_name)
        )

        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =  NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.description = "Beer drunk"

            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }
}