package com.tintracker.ui.project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tintracker.data.model.Project
import com.tintracker.databinding.FragmentProjectsBinding
import com.tintracker.ui.task.TaskActivity
import com.tintracker.utils.hide
import com.tintracker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!
    private val projectViewModel: ProjectViewModel by viewModel()
    private var adapter =
        ProjectListAdapter { project, action -> execAction(project, action) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        val view = binding.root
        addOnClickListener()

        binding.listProject.hasFixedSize()
        binding.listProject.adapter = adapter
        loadProjects()

        return view
    }


    private fun loadProjects() {
        projectViewModel.getProjects()
        projectViewModel.projectsLiveData.observe(requireActivity(), Observer { projects ->
           if (projects.isNotEmpty()) {
               binding.listProject.show()
               binding.noDataImageView.hide()
               binding.noDataTextView.hide()
               adapter.submitList(projects)
           } else {
               binding.listProject.hide()
               binding.noDataImageView.show()
               binding.noDataTextView.show()
           }
        })
    }

    private fun addOnClickListener() {
        binding.flbAdd.setOnClickListener{
            val intent = Intent(requireContext(), ProjectFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun execAction(project: Project, action: ItemProjectClick) {
        when(action) {
            ItemProjectClick.START_TIMER -> {
                val intent = Intent(activity, TaskActivity::class.java)
                intent.putExtra("project", project)
                startActivity(intent)
            }
            ItemProjectClick.DETAIL -> {
                val intent = Intent(activity, ProjectFormActivity::class.java)
                intent.putExtra("project", project)
                startActivity(intent)
            }
        }
    }

}
