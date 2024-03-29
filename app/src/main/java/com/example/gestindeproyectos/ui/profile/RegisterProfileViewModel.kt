package com.example.gestindeproyectos.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class RegisterProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _email = MutableLiveData<String>().apply {
        value = FirebaseAuth.getInstance().currentUser?.email
    }
    val email: MutableLiveData<String> = _email
}