package com.robosh.distancebetween.waitfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.robosh.distancebetween.databinding.FragmentWaitForFriendBinding
import com.robosh.distancebetween.model.User
import com.robosh.distancebetween.waitfriend.viewmodel.WaitForFriendViewModel
import timber.log.Timber

class WaitForFriendFragment : Fragment(), AcceptConnectionDialog.OnAcceptConnectionDialogListener {

    private lateinit var binding: FragmentWaitForFriendBinding
    private lateinit var viewModel: WaitForFriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WaitForFriendViewModel::class.java)
        viewModel.makeCurrentUserAvailableForSharing()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaitForFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.showDialogTest.setOnClickListener {
            showAcceptConnectionDialog(User())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.makeCurrentUserNotAvailableForSharing()
    }

    private fun showAcceptConnectionDialog(user: User) {
        val dialog = AcceptConnectionDialog.newInstance("PEtro")
        dialog.show(childFragmentManager, "example dialog")
    }

    override fun onAcceptButtonClicked() {
        Timber.d("onAcceptButtonClicked")
    }

    override fun onRejectButtonClicked() {
        Timber.d("onRejectButtonClicked")
    }
}