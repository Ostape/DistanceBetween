package com.robosh.distancebetween.database

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.robosh.distancebetween.model.User
import timber.log.Timber
import java.util.*

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

    override fun isUserExistsInDatabase(): LiveData<User> {
        val isUserExists = MutableLiveData<User>()
        val childEventListener = object : ChildEventListener {

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.message)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildMoved")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildChanged")
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user?.id.equals(FirebaseInstanceId.getInstance().id)) {
                    isUserExists.postValue(user)
                }
                Timber.d("onChildAdded")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.d("onChildRemoved")
            }
        }
        reference.addChildEventListener(childEventListener)
        return isUserExists
    }

    override fun saveUser(user: User) {
        val id = FirebaseInstanceId.getInstance().id
        reference.push().setValue(user.apply { user.id = id })
            .addOnSuccessListener {
                // TODO add callback
            }
            .addOnFailureListener {
                Timber.e(it, "Failed to save user")
                // TODO add callback
            }
    }

    // this method returns all users that are available for sharing your location
    override fun getAvailableUsers(): LiveData<List<User>> {
        val availableUsers = ArrayList<User>()
        val availableUsersLiveData =
            MutableLiveData<List<User>>().apply { postValue(availableUsers) }
        val childEventListener = object : ChildEventListener {

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.message)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildMoved")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildChanged")
                val value = snapshot.getValue(User::class.java) ?: return
                val cachedUser = availableUsers.find { it.id == value.id }
                if (value.isUserAvailable) {
                    availableUsers.remove(cachedUser)
                    availableUsers.add(value)
                } else {
                    availableUsers.remove(cachedUser)
                }
                availableUsersLiveData.postValue(availableUsers)
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user?.isUserAvailable == true) {
                    availableUsers.add(user)
                }
                availableUsersLiveData.postValue(availableUsers)
                Timber.d("onChildAdded")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.d("onChildRemoved")
                availableUsers.remove(snapshot.getValue(User::class.java))
                availableUsersLiveData.postValue(availableUsers)
            }
        }
        reference.addChildEventListener(childEventListener)
        return availableUsersLiveData
    }

    // this method updates availability for sharing your location
    override fun setUserAvailability(availability: Boolean): User {
        TODO("Not yet implemented")
    }
}