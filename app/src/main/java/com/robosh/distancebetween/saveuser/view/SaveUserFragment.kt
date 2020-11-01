package com.robosh.distancebetween.saveuser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.robosh.distancebetween.databinding.FragmentSaveUserBinding
import com.robosh.distancebetween.saveuser.viewmodel.SaveUserViewModel

class SaveUserFragment : Fragment() {

    private lateinit var binding: FragmentSaveUserBinding

    private lateinit var viewModel: SaveUserViewModel

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()

            val fragment =
                SaveUserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO add app bar initialisation
        viewModel = ViewModelProviders.of(this).get(SaveUserViewModel::class.java)
        binding = FragmentSaveUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveUsernameEditText.doAfterTextChanged { text ->
            viewModel.username = text?.toString() ?: ""
        }

        viewModel.isFormValid.observe(this, Observer { valid ->
            binding.saveUserInFirebaseButton.isEnabled = valid ?: false
        })

        binding.saveUserInFirebaseButton.setOnClickListener {
            viewModel.saveUser()
        }
    }
}