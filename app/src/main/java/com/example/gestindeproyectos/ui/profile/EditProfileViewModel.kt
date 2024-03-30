package com.example.gestindeproyectos.ui.profile

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.Collaborator
import com.example.gestindeproyectos.model.CollaboratorState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _userProfilePicture = MutableLiveData<Drawable>()
    private val _collaborator = MutableLiveData<Collaborator?>()
    private val _userEmail = MutableLiveData<String>()
    private val _userId = MutableLiveData<String>()
    private val _userName = MutableLiveData<String>()
    private val _userLastName = MutableLiveData<String>()
    private val _userPhone = MutableLiveData<String>()
    private val _userDepartment = MutableLiveData<String>()
    private val _userState = MutableLiveData<String>()
    private val _userProject = MutableLiveData<String>()

    init {
        val storageRef = FirebaseStorage.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val imageRef = storageRef.child("profile_pictures/${currentUser?.uid}")

        // Resize to 200x200, center-crop and load the image
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .resize(1000, 1000)
                .centerCrop()
                .into(object : com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: android.graphics.Bitmap?, from: Picasso.LoadedFrom?) {
                        _userProfilePicture.value = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                        _userProfilePicture.value = BitmapDrawable(android.graphics.Bitmap.createBitmap(1000, 1000, android.graphics.Bitmap.Config.ARGB_8888))
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        _userProfilePicture.value = BitmapDrawable(android.graphics.Bitmap.createBitmap(1000, 1000, android.graphics.Bitmap.Config.ARGB_8888))
                    }
                })
        }.addOnFailureListener {
            _userProfilePicture.value = BitmapDrawable(android.graphics.Bitmap.createBitmap(1000, 1000, android.graphics.Bitmap.Config.ARGB_8888))
        }

        DB.instance.fetchCollaboratorWithEmail(FirebaseAuth.getInstance().currentUser!!.email!!).thenAccept { collaborator ->
            _collaborator.postValue(collaborator)
        }

        // Observer for collaborator changes
        _collaborator.observeForever {
            updateLiveDataValues(it)
        }
    }

    private fun updateLiveDataValues(collaborator: Collaborator?) {
        collaborator?.let {
            _userEmail.value = FirebaseAuth.getInstance().currentUser?.email
            _userId.value = getApplication<Application>().resources.getString(R.string.id) + ": " + it.getIdentification()
            _userName.value = it.getName()
            _userLastName.value = it.getLastName()
            _userPhone.value = it.getPhone()
            _userDepartment.value = it.getDepartment()
            _userState.value = when (it.getState()) {
                CollaboratorState.ACTIVE -> getApplication<Application>().resources.getString(R.string.state) + ": " + getApplication<Application>().resources.getString(R.string.collaborator_state_active)
                CollaboratorState.INACTIVE -> getApplication<Application>().resources.getString(R.string.state) + ": " + getApplication<Application>().resources.getString(R.string.collaborator_state_inactive)
            }
            if (it.getProject().isEmpty()) {
                _userProject.value = getApplication<Application>().resources.getString(R.string.project) + ": " + getApplication<Application>().resources.getString(R.string.no_project)
                return
            }
            DB.instance.fetchProject(it.getProject()).thenAccept { project ->
                _userProject.postValue(getApplication<Application>().resources.getString(R.string.project) + ": " + project?.getName())
            }
        }
    }

    fun fetchData() {
        val email = FirebaseAuth.getInstance().currentUser?.email
        email?.let {
            DB.instance.fetchCollaboratorWithEmail(it).thenAccept { collaborator ->
                _collaborator.postValue(collaborator)
            }
        }
    }

    val userProfilePicture: LiveData<Drawable> = _userProfilePicture
    val userEmail: LiveData<String> = _userEmail
    val userId: LiveData<String> = _userId
    val userName: LiveData<String> = _userName
    val userLastname: LiveData<String> = _userLastName
    val userPhone: LiveData<String> = _userPhone
    val userDepartment: LiveData<String> = _userDepartment
    val userState: LiveData<String> = _userState
    val userProject: LiveData<String> = _userProject
}