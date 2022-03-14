package com.example.individualproject

data class Task(val task: String, var isDone: Boolean) {
    override fun toString(): String {
        return task
    }
}