package com.example.gestindeproyectos.ui.forum

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.Collaborator
import com.example.gestindeproyectos.model.ForumItem
import com.google.firebase.Timestamp

class ForumItemViewModel(application: Application, forumId: String, forumItemId: String, forumAuthorId: String) : AndroidViewModel(application) {

    private val _forumItem = MutableLiveData<ForumItem>()
    private val _forumAuthor = MutableLiveData<Collaborator>()
    private val _forumContent = MutableLiveData<String>()
    private val _forumTimestamp = MutableLiveData<Timestamp>()
    private val _forumReplies = MutableLiveData<List<ForumItem>>()

    init {
        DB.instance.fetchCollaborator(forumAuthorId).thenAccept {
            _forumAuthor.postValue(it)
        }

        DB.instance.fetchForumItem(forumId, forumItemId).thenAccept {
            _forumItem.postValue(it)
        }

        _forumItem.observeForever {
            // updateDataLiveValues(it)
        }
    }

    val forumAuthor: LiveData<Collaborator> = _forumAuthor
    val forumContent: LiveData<String> = _forumContent
    val forumTimestamp: LiveData<Timestamp> = _forumTimestamp
    val forumReplies: LiveData<List<ForumItem>> = _forumReplies
}
