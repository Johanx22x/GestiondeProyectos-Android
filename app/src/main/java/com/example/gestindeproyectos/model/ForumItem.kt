package com.example.gestindeproyectos.model

import com.google.firebase.Timestamp

class ForumItem(
    private val id: String,
    private var authorId: String,
    private var content: String,
    private var timestamp: Timestamp?,
    private var replies: List<ForumItem>
) {
    fun getId(): String {
        return id
    }

    fun getAuthorId(): String {
        return authorId
    }

    fun setAuthorId(authorId: String) {
        this.authorId = authorId
    }

    // fun getAuthor(): Collaborator? {
    //     return author
    // }

    // fun setAuthor(author: Collaborator) {
    //     this.author = author
    // }

    fun getContent(): String {
        return content
    }

    fun setContent(content: String) {
        this.content = content
    }

    fun getTimestamp(): Timestamp? {
        return timestamp
    }

    fun setTimestamp(timestamp: Timestamp) {
        this.timestamp = timestamp
    }

    fun getReplies(): List<ForumItem> {
        return replies
    }

    fun setReplies(replies: List<ForumItem>) {
        this.replies = replies
    }
}