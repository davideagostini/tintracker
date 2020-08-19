package com.tintracker.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tintracker.R
import com.tintracker.data.model.Category
import com.tintracker.databinding.FragmentCategoriesBinding
import com.tintracker.utils.hide
import com.tintracker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModel()
    private var adapter = CategoryListAdapter { item -> editCategory(item) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        addOnClickListener()

        binding.listCategory.hasFixedSize()
        binding.listCategory.adapter = adapter
        loadCategories()
        swipeHandler()

        return view
    }

    private fun loadCategories() {
        categoryViewModel.getCategories()
        categoryViewModel.categoriesLiveData.observe(requireActivity(), Observer { items ->
            if (items.isNotEmpty()) {
                binding.listCategory.show()
                binding.noDataImageView.hide()
                binding.noDataTextView.hide()
                adapter.submitList(items)
            } else {
                binding.listCategory.hide()
                binding.noDataImageView.show()
                binding.noDataTextView.show()
            }
        })
    }

    private fun addOnClickListener() {
        binding.flbAdd.setOnClickListener{
            val intent = Intent(requireContext(), CategoryFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun editCategory(item: Category) {
        val intent = Intent(requireContext(), CategoryFormActivity::class.java)
        intent.putExtra("category", item)
        startActivity(intent)
    }

    private fun swipeHandler() {
        val swipeHandler = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val tempItemToDelete = adapter.currentList[viewHolder.adapterPosition]
                categoryViewModel.deleteCategory(tempItemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                val snackbar = Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    R.string.category_successfully_deleted,
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction("Undo") {
                    categoryViewModel.saveCategory(tempItemToDelete)
                }
                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.listCategory)
    }

}
