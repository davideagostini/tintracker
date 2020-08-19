package com.tintracker.ui.category

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tintracker.R
import com.tintracker.data.model.Category
import com.tintracker.databinding.ActivityFormCategoryBinding
import kotlinx.android.synthetic.main.activity_form_category.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFormActivity : AppCompatActivity() {

    private val categoryViewModel: CategoryViewModel by viewModel()
    private lateinit var binding: ActivityFormCategoryBinding
    private var tempCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent.hasExtra("category")) {
            tempCategory = intent.getParcelableExtra("category") as Category
            if (tempCategory != null) {
                binding.tvCategory.setText(tempCategory?.name)
            }
            supportActionBar?.title = getString(R.string.edit_category)
        } else {
            supportActionBar?.title = getString(R.string.new_category)

        }

        binding.btnSave.setOnClickListener {
            saveCategory()
        }
    }

    private fun saveCategory() {
        val text = binding.tvCategory.text
        if (text.isNullOrEmpty()) {
            tvCategory.background = ContextCompat.getDrawable(applicationContext, R.drawable.input_rounded_error)
            return
        }

        if (tempCategory != null) {
            tempCategory?.name = text.trim().toString()
            categoryViewModel.saveCategory(tempCategory!!)
            tempCategory = null
            Toast.makeText(this, getString(R.string.category_successfully_updated), Toast.LENGTH_SHORT).show()
        } else {
            val category = Category(name= text.trim().toString())
            categoryViewModel.saveCategory(category)
            Toast.makeText(this, getString(R.string.category_successfully_saved), Toast.LENGTH_SHORT).show()
        }
        onBackPressed()
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
}
