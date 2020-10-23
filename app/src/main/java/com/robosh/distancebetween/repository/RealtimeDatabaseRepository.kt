package com.robosh.distancebetween.repository

import android.location.Location
import com.google.firebase.database.*
import com.robosh.distancebetween.model.User

class RealtimeDatabaseRepository : DatabaseRepository {

    private val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference

    init {
        reference = rootNode.getReference("Users")
    }

    companion object {
        private var databaseRepository: DatabaseRepository? = null
        // todo create singleton or use Koin
        fun newInstance(): DatabaseRepository {
            if (databaseRepository == null) {
                databaseRepository = RealtimeDatabaseRepository()
            }
            return databaseRepository!!
        }
    }

    override fun saveLocation(location: Location?) {
        reference.setValue("Second data ${location?.longitude}")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

            }

        })
    }

    override fun isUserExistsInDatabase(userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveUser(user: User): User {
        reference.child("users").push().setValue(User("Petro", "Poroshenko"))
        TODO("add on Complete listener")
    }
}