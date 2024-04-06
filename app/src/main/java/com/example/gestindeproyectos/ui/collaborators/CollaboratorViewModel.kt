package com.example.gestindeproyectos.ui.collaborators

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestindeproyectos.R
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.Collaborator
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class CollaboratorViewModel(application: Application, collaboratorId: String) : AndroidViewModel(application) {

    private val _collaboratorProfilePicture = MutableLiveData<Drawable>()
    private val _collaborator = MutableLiveData<Collaborator?>()
    private val _collaboratorName = MutableLiveData<String>()
    private val _collaboratorEmail = MutableLiveData<String>()
    private val _collaboratorProject = MutableLiveData<String>()
    private val _projects = MutableLiveData<ArrayAdapter<String>>()

    init {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_pictures/${collaboratorId}")
        loadProfilePicture(imageRef)

        DB.instance.fetchCollaboratorWithEmail(collaboratorId).thenAccept { collaborator ->
            _collaborator.postValue(collaborator)
        }

        // Observer for collaborator changes
        _collaborator.observeForever {
            updateLiveDataValues(it)
        }

        _collaboratorProfilePicture.observeForever {
            updateProfilePictureLiveDataValues(collaboratorId)
        }
    }

    private fun loadProfilePicture(imageRef: StorageReference) {
        val placeholderDrawable = ContextCompat.getDrawable(getApplication(), R.drawable.menu_profile)

        // Resize to 200x200, center-crop and load the image
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri)
                .resize(1000, 1000)
                .centerCrop()
                .into(object : com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: android.graphics.Bitmap?, from: Picasso.LoadedFrom?) {
                        _collaboratorProfilePicture.value = BitmapDrawable(bitmap)
                    }

                    override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                        _collaboratorProfilePicture.value = placeholderDrawable!!
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        _collaboratorProfilePicture.value = placeholderDrawable!!
                    }
                })
        }.addOnFailureListener {
            _collaboratorProfilePicture.value = placeholderDrawable!!
        }
    }

    private fun updateLiveDataValues(collaborator: Collaborator?) {
        collaborator?.let {
            _collaboratorName.value = it.getFullName()
            _collaboratorEmail.value = it.getEmail()
            if (it.getProject().isEmpty()) {
                _collaboratorProject.value = "No Project"

                DB.instance.fetchProjects().thenAccept { projects ->
                    val projectNames = projects.map { it.getName() }.toMutableList()
                    projectNames.sort()
                    val adapter = ArrayAdapter(getApplication(), android.R.layout.simple_spinner_item, projectNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    _projects.postValue(adapter)
                }
                return
            }
            DB.instance.fetchProject(it.getProject()).thenAccept { project ->
                _collaboratorProject.postValue(project?.getName())

                DB.instance.fetchProjects().thenAccept { projects ->
                    val projectNames = projects.map { it.getName() }.toMutableList()
                    projectNames.sort()

                    val collaboratorProjectName = _collaboratorProject.value?.toString()
                    val collaboratorProjectIndex = collaboratorProjectName?.let { projectNames.indexOf(it) }

                    // Move collaborator's project to the first position if found
                    if (collaboratorProjectIndex != null && collaboratorProjectIndex != -1) {
                        projectNames.removeAt(collaboratorProjectIndex)
                        projectNames.add(0, collaboratorProjectName)
                    }

                    val adapter = ArrayAdapter(getApplication(), android.R.layout.simple_spinner_item, projectNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    _projects.postValue(adapter)
                }
            }
        }
    }

    private fun updateProfilePictureLiveDataValues(collaboratorId: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_pictures/${collaboratorId}")
        loadProfilePicture(imageRef)
    }

    fun fetchData(collaboratorId: String) {
        DB.instance.fetchCollaborator(collaboratorId).thenAccept { collaborator ->
            _collaborator.postValue(collaborator)
        }
    }

    val collaboratorProfilePicture: LiveData<Drawable> = _collaboratorProfilePicture
    val collaborator: LiveData<Collaborator?> = _collaborator
    val collaboratorName: LiveData<String> = _collaboratorName
    val collaboratorEmail: LiveData<String> = _collaboratorEmail
    val projectList: LiveData<ArrayAdapter<String>> = _projects
}