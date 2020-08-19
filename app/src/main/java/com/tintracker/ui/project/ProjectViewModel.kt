package com.tintracker.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tintracker.data.model.Project
import com.tintracker.data.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.dsl.module

val projectModule = module {
    factory { ProjectViewModel(get()) }
}

class ProjectViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _projectsLiveData = MutableLiveData<List<Project>>()

    val projectsLiveData: LiveData<List<Project>>
        get() = _projectsLiveData

    fun getProjects() {
        viewModelScope.launch {
            projectRepository.getAllProjects().collect {
                _projectsLiveData.value = it
            }
        }
    }

    fun saveProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.saveProject(project)
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteProject(project)
        }
    }
}