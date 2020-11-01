package com.robosh.distancebetween.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.robosh.distancebetween.model.User

class RealtimeDatabaseImpl : RealtimeDatabase {

    private val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference

    init {
        reference = rootNode.getReference("Users")
    }

    companion object {
        private var realtimeDatabase: RealtimeDatabase? = null

        // todo create singleton or use Koin
        fun newInstance(): RealtimeDatabase {
            return realtimeDatabase ?: RealtimeDatabaseImpl().also {
                realtimeDatabase = it
            }
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

    override fun isUserExistsInDatabase(): LiveData<Boolean> {
        // todo rework
        val id = FirebaseInstanceId.getInstance().id
        val isExists = reference.child("users").key.equals(id)
        return MutableLiveData<Boolean>().apply { postValue(isExists) }
    }

    override fun saveUser(user: User): User {
        val id = FirebaseInstanceId.getInstance().id
        reference.child("users").push().setValue(user.apply { user.id = id })
        return User(id, "Petro")
    }

    // this method returns all users that are available for sharing your location
    override fun getAvailableUserIds(): List<String> {
        TODO("Not yet implemented")
    }

    // this method updates availability for sharing your location
    override fun setUserAvailability(availability: Boolean): User {
        TODO("Not yet implemented")
    }
}