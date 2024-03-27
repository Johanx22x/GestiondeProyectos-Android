package com.example.gestindeproyectos.model

class Forum (
    private val id: String,
    private var name: String,
    private var isLocal: Boolean,
    private var items: List<ForumItem>
) {
    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getIsLocal(): Boolean {
        return isLocal
    }

    fun setIsLocal(isLocal: Boolean) {
        this.isLocal = isLocal
    }

    fun getItems(): List<ForumItem> {
        return items
    }

    fun setItems(items: List<ForumItem>) {
        this.items = items
    }
}