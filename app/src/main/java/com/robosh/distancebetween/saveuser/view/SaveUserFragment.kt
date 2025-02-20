package com.robosh.distancebetween.saveuser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.EMPTY_STRING
import com.robosh.distancebetween.application.INTENT_USERNAME
import com.robosh.distancebetween.databinding.FragmentSaveUserBinding
import com.robosh.distancebetween.model.Resource
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.saveuser.viewmodel.SaveUserViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SaveUserFragment : Fragment() {

    private lateinit var binding: FragmentSaveUserBinding
    private val saveUserViewModel: SaveUserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        saveUserViewModel.isUserExistsInDatabase()
            .observe(viewLifecycleOwner, Observer { resource ->
                isUserAlreadyExists(resource)
            })
        binding = FragmentSaveUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveUsernameEditText.doAfterTextChanged { text ->
            saveUserViewModel.username = text?.toString() ?: EMPTY_STRING
        }

        saveUserViewModel.isFormValid.observe(viewLifecycleOwner, Observer { valid ->
            binding.saveUserInFirebaseButton.isEnabled = valid ?: false
        })

        binding.saveUserInFirebaseButton.setOnClickListener {
            listenResultSaveUser()
        }
    }

    // Ideally this should be done by using some Authentication API
    private fun isUserAlreadyExists(resource: Resource<User>) {
        when (resource) {
            is Resource.Error -> Timber.e(resource.message)
            is Resource.Loading -> {
            }
            is Resource.Success -> resource.data?.let {
                navigateToHomeScreenFragment(it)
            }
        }
    }

    private fun navigateToHomeScreenFragment(user: User) {
        findNavController().navigate(
            R.id.action_saveUserFragment_to_homeScreenFragment,
            Bundle().apply {
                putString(INTENT_USERNAME, user.username)
            }
        )
    }

    private fun listenResultSaveUser() {
        saveUserViewModel.saveUser().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Error -> {
                    hideLoadingSpinner()
                    Timber.e(it.message)
                }
                is Resource.Loading -> {
                    showLoadingSpinner()
                }
                is Resource.Success -> {
                    hideLoadingSpinner()
                }
            }
        })
    }

    private fun showLoadingSpinner() {
        binding.progressBar.visibility = VISIBLE
    }

    private fun hideLoadingSpinner() {
        binding.progressBar.visibility = GONE
    }
}