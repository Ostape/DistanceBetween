package com.robosh.distancebetween.waitfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.ACCEPT_CONNECTION_DIALOG_TAG
import com.robosh.distancebetween.application.INTENT_USER_FROM_WAIT_FRIEND
import com.robosh.distancebetween.databinding.FragmentWaitForFriendBinding
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.waitfriend.viewmodel.WaitForFriendViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class WaitForFriendFragment : Fragment(), AcceptConnectionDialog.OnAcceptConnectionDialogListener {

    private lateinit var binding: FragmentWaitForFriendBinding
    private val viewModel: WaitForFriendViewModel by viewModel()
    private var cachedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // todo rename func
        viewModel.makeCurrentUserAvailableForSharing().observe(this, Observer {
            showAcceptConnectionDialog(it)
        })
        viewModel.getCurrentUser().observe(this, Observer {
            cachedUser = it
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

    override fun onAcceptButtonClicked() {
        viewModel.acceptConnection()
        findNavController().navigate(
            R.id.action_waitForFriendFragment_to_locationDistanceFragment,
            Bundle().apply {
                putParcelable(INTENT_USER_FROM_WAIT_FRIEND, cachedUser)
            }
        )
    }

    override fun onRejectButtonClicked() {
        viewModel.rejectConnection()
    }

    private fun showAcceptConnectionDialog(user: User) {
        val dialog = AcceptConnectionDialog.newInstance(user.username)
        dialog.show(childFragmentManager, ACCEPT_CONNECTION_DIALOG_TAG)
    }
}