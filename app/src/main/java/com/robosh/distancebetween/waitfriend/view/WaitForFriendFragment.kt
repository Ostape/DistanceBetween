package com.robosh.distancebetween.waitfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.R
import com.robosh.distancebetween.databinding.FragmentWaitForFriendBinding
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.waitfriend.viewmodel.WaitForFriendViewModel
import timber.log.Timber

class WaitForFriendFragment : Fragment(), AcceptConnectionDialog.OnAcceptConnectionDialogListener {

    private companion object {
        const val ACCEPT_CONNECTION_DIALOG_TAG = "ACCEPT_CONNECTION_DIALOG_TAG"
    }

    private lateinit var binding: FragmentWaitForFriendBinding
    private lateinit var viewModel: WaitForFriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WaitForFriendViewModel::class.java)
        viewModel.makeCurrentUserAvailableForSharing().observe(this, Observer {
            showAcceptConnectionDialog(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaitForFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.makeCurrentUserNotAvailableForSharing()
    }

    private fun showAcceptConnectionDialog(user: User) {
        val dialog = AcceptConnectionDialog.newInstance(user.username)
        dialog.show(childFragmentManager, ACCEPT_CONNECTION_DIALOG_TAG)
    }

    override fun onAcceptButtonClicked() {
        Timber.d("onAcceptButtonClicked")
        viewModel.acceptConnection()
        findNavController().navigate(R.id.action_waitForFriendFragment_to_locationDistanceFragment)
    }

    override fun onRejectButtonClicked() {
        Timber.d("onRejectButtonClicked")
        viewModel.rejectConnection()
    }
}