package com.metalsensor.gold.detector.finder.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.databinding.ItemThemeBinding
import com.metalsensor.gold.detector.finder.model.ThemeModel

class ThemeAdapter(
    private val themes: List<ThemeModel>,
    private var selectedPosition: Int = 0
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {

    inner class ThemeViewHolder(val binding: ItemThemeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val binding = ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        val theme = themes[position]
        holder.binding.themePreview.setImageResource(theme.imageResId)
        // Set background color based on selection
        if (isSelected) {
            holder.binding.bgr.setBackgroundResource(R.color.color_app)
        } else {
            holder.binding.bgr.setBackgroundResource(R.drawable.bgrhome)
        }

        // Dynamically set vertical margins
        val layoutParams = holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
        if (isSelected) {
            layoutParams.setMargins(
                dpToPx(holder.itemView.context, 0),
                dpToPx(holder.itemView.context, 0),
                dpToPx(holder.itemView.context, 0),
                dpToPx(holder.itemView.context, 0)
            )
        } else {
            layoutParams.setMargins(
                dpToPx(holder.itemView.context, 0),
                dpToPx(holder.itemView.context, 30),
                dpToPx(holder.itemView.context, 0),
                dpToPx(holder.itemView.context, 30)
            )
        }
        holder.binding.root.layoutParams = layoutParams

        // Handle item click
        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelectedPosition) // Update previous item
            notifyItemChanged(selectedPosition) // Update current item
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun getItemCount(): Int = themes.size

    // Update selected position from the outside
    fun updateSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
        notifyDataSetChanged()
    }
}
