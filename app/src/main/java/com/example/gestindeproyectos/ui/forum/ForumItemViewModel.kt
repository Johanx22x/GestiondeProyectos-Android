package com.example.gestindeproyectos.ui.forum

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.Collaborator
import com.example.gestindeproyectos.model.ForumItem
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

class ForumItemViewModel(application: Application, forumId: String, forumItemId: String, forumAuthorId: String) : AndroidViewModel(application) {

    private val _forumItem = MutableLiveData<ForumItem>()
    private val _forumItemAuthor = MutableLiveData<Collaborator?>()
    private val _forumItemAuthorName = MutableLiveData<String>()
    private val _forumItemContent = MutableLiveData<String>()
    private val _forumItemDate = MutableLiveData<String>()
    private val _forumItemReplies = MutableLiveData<List<ForumItem>>()

    init {
        DB.instance.fetchCollaborator(forumAuthorId).thenAccept {
            _forumItemAuthor.postValue(it)
        }

        DB.instance.fetchForumItem(forumId, forumItemId).thenAccept {
            _forumItem.postValue(it)
        }

        _forumItem.observeForever {
            updateForumItemDataLiveValues(forumId, it)
        }

        _forumItemAuthor.observeForever {
            updateCollaboratorDataLiveValues(it)
        }
    }

    private fun updateForumItemDataLiveValues(forumId: String, forumItem: ForumItem) {
        forumItem.let {
            _forumItemContent.postValue(it.getContent())
            _forumItemDate.postValue(SimpleDateFormat("dd/MM/yyyy - HH:mm").format(it.getTimestamp()!!.toDate()))
            DB.instance.fetchForumItemReplies(forumId, forumItem.getId()).thenAccept { replies ->
                _forumItemReplies.postValue(replies)
            }
        }
    }

    private fun updateCollaboratorDataLiveValues(forumAuthor: Collaborator?) {
        if (forumAuthor == null) {
            return
        }
        forumAuthor.let {
            _forumItemAuthorName.postValue(it.getFullName())
        }
    }

    val forumItemAuthorName: LiveData<String> = _forumItemAuthorName
    val forumItemContent: LiveData<String> = _forumItemContent
    val forumItemDate: LiveData<String> = _forumItemDate
    val forumItemReplies: LiveData<List<ForumItem>> = _forumItemReplies
}
