package com.robosh.distancebetween.database

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.robosh.distancebetween.model.User
import timber.log.Timber

class RealtimeDatabaseImpl : RealtimeDatabase {

    private val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference: DatabaseReference
    private val currentUserId = FirebaseInstanceId.getInstance().id

    init {
        userReference = rootNode.getReference("Users")
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
        userReference.setValue("Second data ${location?.longitude}")
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
                if (snapshot.key.equals(currentUserId)) {
                    isUserExists.postValue(user)
                }
                Timber.d("onChildAdded")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.d("onChildRemoved")
            }
        }
        userReference.addChildEventListener(childEventListener)
        return isUserExists
    }

    override fun saveUser(user: User) {
        userReference.child(currentUserId).setValue(user)
    }

    // this method returns all users that are available for sharing your location
    override fun getAvailableUsers(): LiveData<List<User>> {
        val availableUsers: MutableMap<String, User> = HashMap()
        val availableUsersLiveData =
            MutableLiveData<List<User>>().apply { postValue(ArrayList(availableUsers.values)) }
        val childEventListener = object : ChildEventListener {

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.message)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildMoved")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildChanged")
                val changedUser = snapshot.getValue(User::class.java) ?: return
                snapshot.key?.let { key ->
                    if (changedUser.isUserAvailable) {
                        availableUsers.put(key, changedUser.apply { id = key })
                    } else {
                        availableUsers.remove(key)
                    }
                }
                availableUsersLiveData.postValue(ArrayList(availableUsers.values))
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user?.isUserAvailable == true && snapshot.key != currentUserId) {
                    snapshot.key?.let {
                        user.id = it
                        availableUsers.put(it, user)
                    }
                }
                availableUsersLiveData.postValue(ArrayList(availableUsers.values))
                Timber.d("onChildAdded")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.d("onChildRemoved")
                availableUsers.remove(snapshot.key)
                availableUsersLiveData.postValue(ArrayList(availableUsers.values))
            }
        }
        userReference.addChildEventListener(childEventListener)
        return availableUsersLiveData
    }

    // this method updates availability for sharing your location
    override fun setUserAvailability(availability: Boolean): User {
        TODO("Not yet implemented")
    }

    override fun setUserAvailabilityAndAddPairedUser(id: String) {
        userReference.child(id).child("userAvailable").setValue(false)
        userReference.child(id).child("connectedFriendId").setValue(currentUserId)
    }

    override fun makeUserAvailableForSharing() {
        userReference.child(currentUserId).child("userAvailable").setValue(true)
    }

    override fun makeUserNotAvailableForSharing() {
        userReference.child(currentUserId).child("userAvailable").setValue(false)
    }
}