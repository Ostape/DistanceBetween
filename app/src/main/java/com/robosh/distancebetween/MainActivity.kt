package com.robosh.distancebetween

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.robosh.distancebetween.application.ACTION_SHOW_DISTANCE_BETWEEN_FRAGMENT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToDistanceBetweenFragment(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToDistanceBetweenFragment(intent)
    }

    private fun navigateToDistanceBetweenFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_DISTANCE_BETWEEN_FRAGMENT) {
            distanceNavHostFragment.findNavController()
                .navigate(R.id.action_global_distance_between_fragment)
        }
    }
}