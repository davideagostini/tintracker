package com.tintracker.ui.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tintracker.R
import com.tintracker.data.model.Project
import com.tintracker.utils.convertIntColorToCircleColor
import com.tintracker.utils.hide
import com.tintracker.utils.show
import kotlinx.android.synthetic.main.item_color.view.*

class ColorListAdapter(
    private val colorList: MutableList<String>,
    private var listener: (String, Boolean) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<ColorListAdapter.ColorHolder>() {

    private var checkedList = HashMap<String, String>()

    init {
        checkedList.put(Project.colors[0], Project.colors[0])
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_color, parent, false)
        return ColorHolder(view)
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.bind(colorList[position])
    }

    fun setColors(colorList: List<String>) {
        this.colorList.addAll(colorList)
        notifyDataSetChanged()
    }

    fun setColor(color: String) {
        checkedList.clear()
        checkedList[color] = color
        notifyDataSetChanged()
    }

    inner class ColorHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(color: String) = with(view) {
            ivColor.background = ContextCompat.getDrawable(context, convertIntColorToCircleColor(color))
            if (checkedList[color] != null) {
                ivCheched.show()
            } else {
                ivCheched.hide()
            }
            ivColor.setOnClickListener {
                if (checkedList[color] == null) {
                    checkedList.clear()
                    checkedList[color] = color
                    ivCheched.show()
                    listener(color, true)
                } else {
                    checkedList.remove(color)
                    ivCheched.hide()
                    listener(color, false)
                }
                notifyDataSetChanged()
            }
        }
    }
}