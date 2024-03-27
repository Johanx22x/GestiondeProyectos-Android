package com.example.gestindeproyectos.model

enum class State(val value: Int) {
    IN_PROGRESS(0),
    PENDING(1),
    FINISHED(2);

    companion object {
        fun fromValue(value: Int): State {
            return when (value) {
                0 -> IN_PROGRESS
                1 -> PENDING
                2 -> FINISHED
                else -> throw IllegalArgumentException("Invalid value")
            }
        }
    }
}