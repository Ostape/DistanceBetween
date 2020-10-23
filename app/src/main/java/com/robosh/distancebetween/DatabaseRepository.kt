package com.robosh.distancebetween

import android.location.Location
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.robosh.distancebetween.model.User

class DatabaseRepository {

    private val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference

    init {
        reference = rootNode.getReference("Users")
    }

    companion object {
        // todo create singleton or use Koin
        fun newInstance(): DatabaseRepository {
            return DatabaseRepository()
        }
    }

    fun saveLocation(location: Location?) {
        reference.setValue("Second data ${location?.longitude}")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

            }

        })
    }

    fun isUserExistsInDatabase() {

    }

    fun saveUser() {
        reference.child("users").push().setValue(User("Petro", "Poroshenko"))
    }
}