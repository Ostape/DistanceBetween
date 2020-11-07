package com.robosh.distancebetween.homescreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.robosh.distancebetween.application.INTENT_USERNAME
import com.robosh.distancebetween.databinding.FragmentHomeScreenBinding
import com.robosh.distancebetween.homescreen.viewmodel.HomeScreenViewModel

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(HomeScreenViewModel::class.java)
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(INTENT_USERNAME)
        binding.usernameTextView.text = username
    }

}