package com.tintracker.data.dao

import androidx.room.*
import com.tintracker.data.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM ${Project.TABLE_NAME}")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Insert
    fun insertAll(list: List<Project>)
}