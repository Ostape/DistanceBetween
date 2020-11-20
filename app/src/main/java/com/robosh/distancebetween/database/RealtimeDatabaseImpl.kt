package com.robosh.distancebetween.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.robosh.distancebetween.model.LocationCoordinates
import com.robosh.distancebetween.model.Resource
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
        userReference.child(currentUserId).child("location").setValue(locationCoordinates)
    }

    override fun isUserExistsInDatabase(): LiveData<Resource<User>> {
        val isUserExists = MutableLiveData<Resource<User>>()
            .apply { postValue(Resource.Loading()) }

        val childEventListener = object : ChildEventListener {

            override fun onCancelled(error: DatabaseError) {
                isUserExists.postValue(Resource.Error(error.message))
                Timber.e(error.message)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildMoved")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("onChildChanged")
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java) ?: return
                if (snapshot.key.equals(currentUserId)) {
                    isUserExists.postValue(Resource.Success(user))
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

    override fun saveUser(user: User): LiveData<Resource<User>> {
        val resourceResult =
            MutableLiveData<Resource<User>>().apply { postValue(Resource.Loading()) }
        userReference.child(currentUserId).setValue(user).addOnSuccessListener {
            resourceResult.postValue(Resource.Success(user))
        }.addOnFailureListener {
            resourceResult.postValue(Resource.Error(it.message.toString()))
        }
        return resourceResult
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

    override fun listenUserChanges(): LiveData<List<User>> {
        val connectedUsers = MutableLiveData<List<User>>()
            .apply { value = mutableListOf() }

//        connectedUsers.postValue()
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
                val user = snapshot.getValue(User::class.java) ?: return
                if (snapshot.key.equals(currentUserId)) {
                }
                Timber.d("onChildAdded")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.d("onChildRemoved")
            }
        }

        return connectedUsers
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