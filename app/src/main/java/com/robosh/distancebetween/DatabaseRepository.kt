package com.robosh.distancebetween

import android.location.Location
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatabaseRepository {

    private lateinit var rootNode: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    init {
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference("Users")
    }

    companion object {
        // todo create singleton or use Koin
        fun newInstance(): DatabaseRepository {
            return DatabaseRepository()
        }
    }

    fun setData(location: Location?) {
        reference.setValue("Second data ${location?.longitude}")
    }

}