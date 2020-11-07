package com.robosh.distancebetween.homescreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosh.distancebetween.databinding.FragmentHomeScreenBinding

class HomeScreenFragment : Fragment() {

    companion object {
        fun newInstance(): HomeScreenFragment {
            return HomeScreenFragment()
        }
    }

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProviders.of(this).get(SaveUserViewModel::class.java)
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

}