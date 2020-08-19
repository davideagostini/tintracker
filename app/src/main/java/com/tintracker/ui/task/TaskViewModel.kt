package com.tintracker.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tintracker.data.model.Category
import com.tintracker.data.model.Task
import com.tintracker.data.repository.CategoryRepository
import com.tintracker.data.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.dsl.module

val taskModule = module {
    factory { TaskViewModel(get(), get()) }
}

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _tasksLiveData = MutableLiveData<List<Task>>()

    val tasksLiveData: LiveData<List<Task>>
        get() = _tasksLiveData

    fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().collect {
                _tasksLiveData.value = it
            }
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.saveTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(task)
        }
    }

    private val _categoriesLiveData = MutableLiveData<List<Category>>()

    val categoriesLiveData: LiveData<List<Category>>
        get() = _categoriesLiveData

    fun getCategories() {
        viewModelScope.launch {
            categoryRepository.gellAllCategories().collect {
                _categoriesLiveData.value = it
            }
        }
    }
}