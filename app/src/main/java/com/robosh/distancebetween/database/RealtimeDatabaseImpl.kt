package com.robosh.distancebetween.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.robosh.distancebetween.model.LocationCoordinates
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

    override fun saveLocation(locationCoordinates: LocationCoordinates) {
//        val myLocation =
//            MyLocation(longitude = location?.longitude ?: 0.0, latitude = location?.latitude ?: 0.0)
        userReference.child(currentUserId).child("location").setValue(locationCoordinates)
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

    override fun setUserAvailabilityAndAddPairedUser(id: String) {
        userReference.child(id).child("userAvailable").setValue(false)
        userReference.child(id).child("connectedFriendId").setValue(currentUserId)
    }

    override fun makeUserAvailableForSharing(): LiveData<User> {
        val userLiveData = MutableLiveData<User>()
        userReference.child(currentUserId).child("userAvailable").setValue(true)
        userReference.child(currentUserId).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    snapshot.key?.let {
                        user.id = it
                    }
                }
                userLiveData.postValue(user)
            }
        })
        return userLiveData
    }

    override fun makeUserNotAvailableForSharing() {
        userReference.child(currentUserId).child("userAvailable").setValue(false)
        userReference.child(currentUserId).child("connectedFriendId").setValue("")
    }

    override fun getUserById(id: String): LiveData<User> {
        val user = MutableLiveData<User>()
        userReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key == id) {
                    user.postValue(snapshot.getValue(User::class.java))
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
        return user
    }

    override fun rejectUserConnection() {
        userReference.child(currentUserId).child("userAvailable").setValue(true)
        userReference.child(currentUserId).child("connectedFriendId").setValue("")
    }

    override fun acceptUserConnection(
        requestedUser: User?,
        currentUser: User?
    ) {
        if (currentUser != null) {
            userReference.child(currentUser.connectedFriendId).child("connectedFriendId")
                .setValue(currentUserId)
        }
    }

    override fun getCurrentUser(): LiveData<User> {
        return getUserByIdListen(currentUserId)
    }

    override fun getConnectedUser(connectedUserId: String): LiveData<User> {
        return getUserByIdListen(connectedUserId)
    }

    private fun getUserByIdListen(id: String): LiveData<User> {
        val user: MutableLiveData<User> = MutableLiveData()
        userReference.child(id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                user.postValue(snapshot.getValue(User::class.java))
            }
        })
        return user
    }
}