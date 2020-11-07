package com.robosh.distancebetween.saveuser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.INTENT_USERNAME
import com.robosh.distancebetween.databinding.FragmentSaveUserBinding
import com.robosh.distancebetween.saveuser.viewmodel.SaveUserViewModel
import timber.log.Timber

class SaveUserFragment : Fragment() {

    private lateinit var binding: FragmentSaveUserBinding

    private lateinit var viewModel: SaveUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO add app bar initialis
        viewModel = ViewModelProviders.of(this).get(SaveUserViewModel::class.java)
        viewModel.isUserExistsInDatabase().observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                Timber.d("User has already exists in Database")
                val bundle = Bundle().apply {
                    putString(INTENT_USERNAME, user.username)
                }
                findNavController().navigate(
                    R.id.action_saveUserFragment_to_homeScreenFragment,
                    bundle
                )
            }
        })
        binding = FragmentSaveUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveUsernameEditText.doAfterTextChanged { text ->
            viewModel.username = text?.toString() ?: ""
        }

        viewModel.isFormValid.observe(viewLifecycleOwner, Observer { valid ->
            binding.saveUserInFirebaseButton.isEnabled = valid ?: false
        })

        binding.saveUserInFirebaseButton.setOnClickListener {
            viewModel.saveUser()
        }
    }
}