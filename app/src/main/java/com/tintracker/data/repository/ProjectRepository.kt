package com.tintracker.data.repository

import com.tintracker.data.dao.ProjectDao
import com.tintracker.data.model.Project
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

val projectRepositoryModule = module {
    factory { ProjectRepository(get()) }
}

class ProjectRepository(private val projectDao: ProjectDao) {

    fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAllProjects()
    }

    suspend fun saveProject(project: Project) {
        projectDao.saveProject(project)
    }

    suspend fun deleteProject(project: Project) {
        projectDao.deleteProject(project)
    }
}