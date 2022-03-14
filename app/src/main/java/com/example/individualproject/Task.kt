package com.example.individualproject

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity(primaryKeys = ["task"])
data class Task(@ColumnInfo var task: String,
                @ColumnInfo var isDone: Boolean) {

    override fun toString(): String {
        return task
    }
}
