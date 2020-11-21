package com.robosh.distancebetween.waitfriend.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.robosh.distancebetween.R
import com.robosh.distancebetween.application.INTENT_ACCEPT_DIALOG_USERNAME
import timber.log.Timber

class AcceptConnectionDialog : AppCompatDialogFragment() {

    internal interface OnAcceptConnectionDialogListener {

        fun onAcceptButtonClicked()

        fun onRejectButtonClicked()
    }

    private lateinit var onAcceptConnectionDialogListener: OnAcceptConnectionDialogListener
    private var username: String? = null

    companion object {
        fun newInstance(username: String): AppCompatDialogFragment {
            return AcceptConnectionDialog().apply {
                arguments = Bundle().apply { putString(INTENT_ACCEPT_DIALOG_USERNAME, username) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString(INTENT_ACCEPT_DIALOG_USERNAME)
        try {
            onAcceptConnectionDialogListener = parentFragment as OnAcceptConnectionDialogListener
        } catch (e: ClassCastException) {
            Timber.e(
                e,
                "Parent Fragment should implement OnAcceptConnectionDialogListener Callback"
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_accept_connection, null)
        val positiveListener = createOnPositiveClickListener()
        val negativeListener = createOnNegativeClickListener()

        builder.setView(view)
            .setTitle(getString(R.string.connection_user_request))
            .setNegativeButton(getString(R.string.reject_connection), negativeListener)
            .setPositiveButton(getString(R.string.accept_connection), positiveListener)

        view.findViewById<TextView>(R.id.usernameDialogTextView).text =
            getString(R.string.connection_request_message, username)
        return builder.create()
    }

    private fun createOnPositiveClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener() { _, _ ->
            onAcceptConnectionDialogListener.onAcceptButtonClicked()
        }
    }

    private fun createOnNegativeClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener() { _, _ ->
            onAcceptConnectionDialogListener.onRejectButtonClicked()
        }
    }
}