package com.example.individualproject

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("DELETE FROM task WHERE isDone = 1")
    suspend fun deleteDone()

    @Query("DELETE FROM task WHERE isDone = 0")
    suspend fun deleteTodos()

    @Delete
    fun delete(task: Task)

    @Delete
    fun deleteAll(vararg tasks: Task)

    @Insert
    fun insertAll(vararg tasks: Task)

    @Update
    fun updateUsers(vararg tasks: Task)


}
