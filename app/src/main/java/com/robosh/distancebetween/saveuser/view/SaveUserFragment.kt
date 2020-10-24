package com.robosh.distancebetween.saveuser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robosh.distancebetween.R

class SaveUserFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_save_user, container, false)
    }

}