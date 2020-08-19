package com.tintracker.data.repository

import com.tintracker.data.dao.TaskDao
import com.tintracker.data.model.Task
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

val taskRepositoryModule = module {
    factory { TaskRepository(get()) }
}

class TaskRepository (private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    suspend fun saveTask(task: Task) {
        taskDao.saveTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    fun statisticsWeek(start: Long, end: Long): Flow<List<Task>> {
        return taskDao.statisticsWeek(start, end)
    }

    fun statisticsDay(start: Long, end: Long): Flow<List<Task>> {
        return taskDao.statisticsDay(start, end)
    }
}