package com.robosh.distancebetween.waitfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosh.distancebetween.databinding.FragmentWaitForFriendBinding

class WaitForFriendFragment : Fragment() {

    private lateinit var binding: FragmentWaitForFriendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaitForFriendBinding.inflate(inflater, container, false)
        return binding.root
    }
}