package com.tintracker.data.dao

import androidx.room.*
import com.tintracker.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM ${Task.TABLE_NAME}")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM ${Task.TABLE_NAME} WHERE from_time BETWEEN :dayst AND :dayet and to_time BETWEEN :dayst AND :dayet")
    fun statisticsDay(dayst: Long, dayet: Long): Flow<List<Task>>

    @Query("SELECT * FROM ${Task.TABLE_NAME} WHERE from_time BETWEEN :dayst AND :dayet")
    fun statisticsWeek(dayst: Long, dayet: Long): Flow<List<Task>>

}