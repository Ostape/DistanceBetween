package com.robosh.distancebetween.waitfriend.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosh.distancebetween.databinding.FragmentWaitForFriendBinding
import timber.log.Timber

class WaitForFriendFragment : Fragment(), AcceptConnectionDialog.OnAcceptConnectionDialogListener{

    private lateinit var binding: FragmentWaitForFriendBinding

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
            showAcceptConnectionDialog()
        }
    }

    private fun showAcceptConnectionDialog() {
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