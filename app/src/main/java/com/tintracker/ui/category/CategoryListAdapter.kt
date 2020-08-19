package com.tintracker.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tintracker.R
import com.tintracker.data.model.Category
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryListAdapter(
    private var listener: (Category) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Category, CategoryListAdapter.CategoryHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.category_item, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(category: Category) = with(view) {
            tvName.text = category.name
            layoutItemCategory.setOnClickListener {
                listener(category)
            }
        }
    }

    private object DiffCallback: DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }
    }
}