package com.tintracker.ui.project

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tintracker.R
import com.tintracker.data.model.Project
import com.tintracker.data.model.StatusProject
import com.tintracker.databinding.ActivityProjectFormBinding
import com.tintracker.utils.getListPositionByColor
import com.tintracker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProjectFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectFormBinding
    private val projectViewModel: ProjectViewModel by viewModel()
    private var adapter = ColorListAdapter(mutableListOf()) { color, status -> colorClicked(color, status)}
    private var tempProject: Project? = null
    private var tempColor: String? = null
    private var defaultColor: String = Project.colors[1]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.listColor.adapter = adapter
        adapter.setColors(Project.colors)

        if (intent.hasExtra("project")) {
            tempProject = intent.getParcelableExtra("project") as Project
            if (tempProject != null) {
                supportActionBar?.title = getString(R.string.edit_project)
                binding.tvClient.setText(tempProject!!.clientName)
                binding.tvProject.setText(tempProject!!.projectName)
                binding.tvHourlyRate.setText(tempProject!!.hourlyRate.toString())
                tempColor = tempProject!!.color
                adapter.setColor(tempProject!!.color!!)
                binding.listColor.scrollToPosition(getListPositionByColor(tempProject!!.color!!))
                binding.btnStatusProject.text = getStatusProject(tempProject?.status!!)
                binding.labelStatusProject.show()
                binding.btnStatusProject.show()
                binding.btnDelete.show()
            }
            changeStatusProject()
        } else {
            supportActionBar?.title = getString(R.string.new_project)
            adapter.setColor(defaultColor)
        }

        binding.btnSave.setOnClickListener {
            saveProject()
        }
        binding.btnDelete.setOnClickListener {
            deleteProject()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun colorClicked(color: String, status: Boolean) {
        tempColor = if (status) color
        else null
    }

    private fun saveProject() {
        val tvProject = binding.tvProject.text
        val tvClient = binding.tvClient.text
        val tvHourlyRate = binding.tvHourlyRate.text

        if (tvProject.isNullOrEmpty()) {
            binding.tvProject.background = ContextCompat.getDrawable(applicationContext, R.drawable.input_rounded_error)
        }
        if (tvClient.isNullOrEmpty()) {
            binding.tvClient.background = ContextCompat.getDrawable(applicationContext, R.drawable.input_rounded_error)
        }
        if (tvHourlyRate.isNullOrEmpty()) {
            binding.tvHourlyRate.background = ContextCompat.getDrawable(applicationContext, R.drawable.input_rounded_error)
        }

        if (tvClient!!.isNotEmpty() && tvProject!!.isNotEmpty() && tvHourlyRate!!.isNotEmpty()) {
            if (tempProject != null) {
                tempProject?.clientName = tvClient.toString()
                tempProject?.projectName = tvProject.toString()
                tempProject?.hourlyRate = tvHourlyRate.toString().toFloat()
                tempProject?.color = if (tempColor != null) tempColor else defaultColor
                projectViewModel.saveProject(tempProject!!)
                Toast.makeText(this, getString(R.string.project_successfully_updated), Toast.LENGTH_SHORT).show()
            } else {
                tempProject = Project(
                    clientName = tvClient.toString(),
                    projectName = tvProject.toString(),
                    hourlyRate = tvHourlyRate.toString().toFloat(),
                    color = if (tempColor != null) tempColor else defaultColor
                )
                projectViewModel.saveProject(tempProject!!)
                Toast.makeText(this, getString(R.string.project_successfully_saved), Toast.LENGTH_SHORT).show()
            }
            onBackPressed()
        }
    }

    private fun changeStatusProject() {
        binding.btnStatusProject.setOnClickListener {
            if (tempProject?.status!!.name == StatusProject.ONGOING.name) {
                tempProject?.status = StatusProject.CLOSED
                binding.btnStatusProject.text = getString(R.string.closed)
            } else {
                tempProject?.status = StatusProject.ONGOING
                binding.btnStatusProject.text = getString(R.string.ongoing)
            }
        }
    }

    private fun getStatusProject(status: StatusProject): String {
        return when(status) {
            StatusProject.ONGOING -> getString(R.string.ongoing)
            StatusProject.CLOSED -> getString(R.string.closed)
        }
    }

    private fun deleteProject() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.attention))
        builder.setMessage(getString(R.string.delete_project_msg))
        builder.setPositiveButton("Yes") { dialog, which ->
            Toast.makeText(applicationContext, getString(R.string.project_successfully_deleted), Toast.LENGTH_LONG).show()
            projectViewModel.deleteProject(tempProject!!)
            finish()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }


}