package com.example.respira

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.respira.databinding.ItemTechniqueBinding

class TechniqueAdapter(
    private val onEditClick: (BreathingTechnique) -> Unit,
    private val onDeleteClick: (BreathingTechnique) -> Unit
) : ListAdapter<BreathingTechnique, TechniqueAdapter.TechniqueViewHolder>(TechniqueDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechniqueViewHolder {
        val binding = ItemTechniqueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TechniqueViewHolder(binding, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: TechniqueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TechniqueViewHolder(
        val binding: ItemTechniqueBinding,
        private val onEditClick: (BreathingTechnique) -> Unit,
        private val onDeleteClick: (BreathingTechnique) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(technique: BreathingTechnique) {
            binding.tvItemTitle.text = technique.title

            binding.tvItemPattern.text = binding.root.context.getString(
                R.string.pattern_format,
                technique.inhale,
                technique.hold,
                technique.exhale
            )

            binding.btnOptions.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.menu.add("Edit")
                popup.menu.add("Delete")

                popup.setOnMenuItemClickListener { item ->
                    when (item.title) {
                        "Edit" -> onEditClick(technique)
                        "Delete" -> onDeleteClick(technique)
                    }
                    true
                }
                popup.show()
            }
        }
    }

    class TechniqueDiffCallback : DiffUtil.ItemCallback<BreathingTechnique>() {
        override fun areItemsTheSame(oldItem: BreathingTechnique, newItem: BreathingTechnique): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BreathingTechnique, newItem: BreathingTechnique): Boolean {
            return oldItem == newItem
        }
    }
}