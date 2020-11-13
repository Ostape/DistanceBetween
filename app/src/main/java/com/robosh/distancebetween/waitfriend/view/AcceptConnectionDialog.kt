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
            val arg = Bundle()
            arg.putString(INTENT_ACCEPT_DIALOG_USERNAME, username)
            return AcceptConnectionDialog().apply { arguments = arg }
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

    // todo extract to strings
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_accept_connection, null)
        val positiveListener = createOnPositiveClickListener()
        val negativeListener = createOnNegativeClickListener()

        builder.setView(view)
            .setTitle("Title")
            .setNegativeButton("Reject", negativeListener)
            .setPositiveButton("Accept", positiveListener)

        view.findViewById<TextView>(R.id.usernameDialogTextView).text =
            "$username wants to share location with you!"
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