package com.tintracker.ui.task

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tintracker.R
import com.tintracker.data.model.Category
import com.tintracker.data.model.Project
import com.tintracker.data.model.Task
import com.tintracker.databinding.ActivityTaskBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private val taskViewModel: TaskViewModel by viewModel()
    private lateinit var categoryList: List<Category>
    private var task = Task(from = Date().time)
    private var tempProject: Project? = null
    private lateinit var roundingAlone: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.chronometer.start()
        roundingAlone = AnimationUtils.loadAnimation(applicationContext, R.anim.roundingalone)
        binding.icanchor.startAnimation(roundingAlone)

        getCategories()
        clickSaveButton()

        if (intent.hasExtra("project")) {
            tempProject = intent.getParcelableExtra("project") as Project
        }
        binding.tvProject.text = tempProject?.projectName
        binding.btnClose.setOnClickListener{onBackPressed()}
    }

    private fun getCategories() {
        taskViewModel.getCategories()
        taskViewModel.categoriesLiveData.observe(this, Observer { state ->
            if (state.isNotEmpty()) {
                categoryList = state
                val body = mutableListOf<String>()
                for (i in state) {
                    body.add(i.name)
                }
                val spinnerArrayAdapter = ArrayAdapter(applicationContext, R.layout.spinner_white, body)
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item)
                binding.categorySpinner.adapter = spinnerArrayAdapter
            }
        })
    }

    private fun clickSaveButton() {
        binding.flbSaveTask.setOnClickListener {
            binding.chronometer.stop()
            task.description = "Short description"
            task.projectId = tempProject?.id
            task.category = categoryList[binding.categorySpinner.selectedItemPosition]
            task.to = Date().time + (2000000..5000000).random()
            taskViewModel.saveTask(task)
            Toast.makeText(this, getString(R.string.task_successfully_saved), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

}